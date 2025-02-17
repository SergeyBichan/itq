package testtask.orders.service.impl;

import exception.OrderNumberNotRecievedException;
import exception.ResourceNotFoundException;
import exception.ResourceSaveFailed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.dto.OrderDtoWithoutDetails;
import testtask.orders.dto.mapper.OrderDetailsMapper;
import testtask.orders.dto.mapper.OrderMapper;
import testtask.orders.entity.Order;
import testtask.orders.entity.OrderDetails;
import testtask.orders.repository.OrderDetailsRepository;
import testtask.orders.repository.OrderRepository;
import testtask.orders.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static constants.GlobalConstatnt.*;
import static testtask.orders.service.util.UtilMethods.calculateTotalAmountForOrder;
import static testtask.orders.service.util.UtilMethods.getStringFromFormattedDate;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final RestTemplate restTemplate;
    private final OrderMapper orderMapper;

    @Value("${service.generate-number.url}")
    private String uriForGenerateNumber;
    @Value("${service.generate-number.correct-date-format}")
    private String correctDateFormat;

    private final OrderRepository orderRepository;

    private final OrderDetailsRepository orderDetailsRepository;

    private final OrderDetailsMapper orderDetailsMapper;

    @Transactional
    @Override
    public String createOrder(OrderDtoForCreateOrder orderDtoForCreateOrder) {
        String generatedOrderNumber = Optional.ofNullable(restTemplate.getForObject(uriForGenerateNumber, String.class))
                .orElseThrow(
                () -> new OrderNumberNotRecievedException(ORDER_NUMBER_NOT_RECIEVED)
        );

        String dateNow = generatedOrderNumber.substring(5);
        LocalDate date = LocalDate.parse(dateNow, DateTimeFormatter.ofPattern(correctDateFormat));

        Order order = orderMapper
                .toEntity(orderDtoForCreateOrder, date, generatedOrderNumber,
                        calculateTotalAmountForOrder(orderDtoForCreateOrder));

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new ResourceSaveFailed(ORDER_SAVE_FAILED);
        }

        Order byOrderNumberFromDb = Optional.ofNullable(orderRepository
                .findByOrderNumber(generatedOrderNumber)).orElseThrow(
                        () -> new ResourceNotFoundException(ORDER_NOT_FOUND)
        );

        orderDtoForCreateOrder.getOrderDetails().forEach(od -> {
            OrderDetails orderDetails = orderDetailsMapper.toOrderDetailsEntity(od, byOrderNumberFromDb);
            try {
                orderDetailsRepository.save(orderDetails);
            } catch (Exception e) {
                throw new ResourceSaveFailed(ORDER_SAVE_FAILED);
            }
        });

        return "Order " + generatedOrderNumber + " created successfully";
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Objects.requireNonNull(id, "id must not be null");

        Order orderFromDb = Optional.ofNullable(orderRepository.findById(id)).orElseThrow(
                () -> new ResourceNotFoundException(ORDER_NOT_FOUND));

        List<OrderDetails> allByOrderId = Optional.ofNullable(orderDetailsRepository
                .findAllByOrderId(orderFromDb.getId())).orElseThrow(
                        () -> new ResourceNotFoundException(ORDER_NOT_FOUND)
        );

        List<OrderDetailsDto> orderDetailsDtoList = allByOrderId
                .stream()
                .map(orderDetailsMapper::toOrderDetailsDto)
                .toList();

        return orderMapper.toTDtoWithDetails(orderFromDb, orderDetailsDtoList);
    }

    public List<OrderDtoWithoutDetails> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDtoWithoutDetails)
                .toList();
    }

    public List<OrderDto> getOrdersByDateAndMoreThanTotalAmount(LocalDate date, BigDecimal amount) {

        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(amount, "amount must not be null");

        String localDate = getStringFromFormattedDate(date);

        return Optional.ofNullable(orderRepository.findOrdersByDateAndMoreThanTotalAmount(localDate, amount))
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND))
                .stream()
                .map(order -> {
                    List<OrderDetailsDto> orderDetailsDtoList = orderDetailsRepository.findAllByOrderId(order.getId())
                            .stream()
                            .map(orderDetailsMapper::toOrderDetailsDto)
                            .toList();
                    return orderMapper.toTDtoWithDetails(order, orderDetailsDtoList);
                })
                .toList();
    }

    public List<OrderDto> getAllOrdersBetweenDatesAndExcludingProduct(String productName, LocalDate start, LocalDate end) {
        Objects.requireNonNull(productName, "productName must not be null");
        Objects.requireNonNull(start, "start date must not be null");
        Objects.requireNonNull(end, "end date must not be null");

        String startDate = getStringFromFormattedDate(start);
        String endDate = getStringFromFormattedDate(end);

        return Optional.ofNullable(orderRepository
                        .findOrdersBetweenDatesAndExcludingProduct(productName, startDate, endDate))
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND))
                .stream()
                .map(order -> {
                    List<OrderDetailsDto> orderDetailsDtoList = orderDetailsRepository
                            .findAllProductsExcludingProductName(productName, order.getId())
                            .stream()
                            .map(orderDetailsMapper::toOrderDetailsDto)
                            .toList();
                    return orderMapper.toTDtoWithDetails(order, orderDetailsDtoList);
                })
                .toList();
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Objects.requireNonNull(id, "id must not be null");

        Order byId = Optional.ofNullable(orderRepository.findById(id)).orElseThrow(
                () -> new ResourceNotFoundException(ORDER_NOT_FOUND)
        );

        orderDetailsRepository.deleteAllByOrderId(byId.getId());
        orderRepository.deleteById(byId.getId());

    }

}
