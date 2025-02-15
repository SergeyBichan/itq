package testtask.orders.service.impl;

import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
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
import testtask.orders.repository.impl.OrderRepositoryImpl;
import testtask.orders.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static constants.GlobalConstatnt.ORDER_NOT_FOUND;
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
        String generatedOrderNumber = restTemplate.getForObject(uriForGenerateNumber, String.class);
        if (generatedOrderNumber == null) {
            throw new RuntimeException("Generate order number failed");
        }
        String dateNow = generatedOrderNumber.substring(5);
        LocalDate date = LocalDate.parse(dateNow, DateTimeFormatter.ofPattern(correctDateFormat));

        Order order = orderMapper
                .toEntity(orderDtoForCreateOrder, date, generatedOrderNumber,
                        calculateTotalAmountForOrder(orderDtoForCreateOrder));

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            log.error("cannot save order {}", orderDtoForCreateOrder);
            throw new RuntimeException("cannot save order", e.getCause());
        }

        Order byOrderNumberFromDb = orderRepository
                .findByOrderNumber(generatedOrderNumber);
        if (byOrderNumberFromDb == null) {
            log.error("order number {} not found", generatedOrderNumber);
            throw new RuntimeException("Order number not found");
        }

        orderDtoForCreateOrder.getOrderDetails().forEach(od -> {
            OrderDetails orderDetails = orderDetailsMapper.toOrderDetailsEntity(od, byOrderNumberFromDb);
            orderDetailsRepository.save(orderDetails);
        });

        return "Order " + generatedOrderNumber + " created successfully";
    }

    @Override
    public OrderDto getOrderById(Long id) {
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
        if (date == null || amount == null) {
            throw new RuntimeException("Date and amount must be greater than 0");
        }
        String localDate = getStringFromFormattedDate(date);

        List<Order> ordersByDateAndMoreThanTotalAmount = Optional.ofNullable(orderRepository
                .findOrdersByDateAndMoreThanTotalAmount(localDate, amount)).orElseThrow(
                        () -> new ResourceNotFoundException(ORDER_NOT_FOUND)
        );

        List<OrderDto> orderDtoList = new ArrayList<>();

        ordersByDateAndMoreThanTotalAmount.forEach(order -> {
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByOrderId(order.getId());
            List<OrderDetailsDto> orderDetailsDtoList = orderDetailsList.stream()
                    .map(orderDetailsMapper::toOrderDetailsDto)
                    .toList();
            OrderDto orderDto = orderMapper.toTDtoWithDetails(order, orderDetailsDtoList);
            orderDtoList.add(orderDto);
        });

        return orderDtoList;
    }

    public List<OrderDto> getAllOrdersBetweenDatesAndExcludingProduct(String productName, LocalDate start, LocalDate end) {
        if (start == null || end == null || productName == null) {
            log.error("Start {} date and end date {} and product name is null", start, end);
            throw new RuntimeException("Date and amount must be greater than 0");
        }
        String startDate = getStringFromFormattedDate(start);
        String endDate = getStringFromFormattedDate(end);

        List<Order> ordersBetweenDatesAndExcludingProduct = orderRepository
                .findOrdersBetweenDatesAndExcludingProduct(productName, startDate, endDate);
        if (ordersBetweenDatesAndExcludingProduct.isEmpty()) {
            log.error("No orders between {} and {} found", productName, startDate);
            throw new RuntimeException("No orders between date and product " + productName);
        }

        List<OrderDto> orderDtoList = new ArrayList<>();

        ordersBetweenDatesAndExcludingProduct.forEach(order -> {
            List<OrderDetails> orderDetails = orderDetailsRepository
                    .findAllProductsExcludingProductName(productName, order.getId());
            List<OrderDetailsDto> orderDetailsDtoList = orderDetails
                    .stream()
                    .map(orderDetailsMapper::toOrderDetailsDto)
                    .toList();
            OrderDto orderDto = orderMapper.toTDtoWithDetails(order, orderDetailsDtoList);
            orderDtoList.add(orderDto);
        });

        return orderDtoList;
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        if (id == null) {
            throw new RuntimeException("id is null");
        }

        Order byId = orderRepository.findById(id);
        if (byId == null) {
            log.error("Order with id {} not found", id);
            throw new RuntimeException("Order not found");
        }

        try {
            orderDetailsRepository.deleteAllByOrderId(id);
        } catch (Exception e) {
            log.error("Error while saving", e.getMessage());
            throw new RuntimeException("Order save failed");
        }

        try {
            orderRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error while deleting", e.getMessage());
            throw new RuntimeException("Order not found");
        }

    }

}
