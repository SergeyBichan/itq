package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.dto.mapper.OrderDetailsMapper;
import testtask.orders.dto.mapper.OrderMapper;
import testtask.orders.entity.Order;
import testtask.orders.entity.OrderDetails;
import testtask.orders.repository.OrderDetailsRepository;
import testtask.orders.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${service.generate-number.url}")
    private String uriForGenerateNumber;
    @Value("${service.generate-number.correct-date-format}")
    private String correctDateFormat;

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final RestTemplate restTemplate;
    private final OrderMapper orderMapper;
    private final OrderDetailsMapper orderDetailsMapper;


    public String createOrder(OrderDtoForCreateOrder orderDtoForCreateOrder) {
        String generatedOrderNumber = restTemplate.getForObject(uriForGenerateNumber, String.class);
        if (generatedOrderNumber == null) {
            throw new RuntimeException("Generate order number failed");
        }
        String dateNow = generatedOrderNumber.substring(5);
        LocalDate date = LocalDate.parse(dateNow, DateTimeFormatter.ofPattern(correctDateFormat));

        Order order = Order.builder()
                .orderNumber(generatedOrderNumber)
                .totalAmount(getReduce(orderDtoForCreateOrder))
                .orderDate(date)
                .orderConsumer(orderDtoForCreateOrder.getOrderConsumer())
                .deliveryAddress(orderDtoForCreateOrder.getDeliveryAddress())
                .paymentMethod(orderDtoForCreateOrder.getPaymentMethod())
                .deliveryMethod(orderDtoForCreateOrder.getDeliveryMethod())
                .build();

        orderRepository.save(order);

        Order byOrderNumberFromDb = orderRepository.findByOrderNumber(generatedOrderNumber);

        orderDtoForCreateOrder.getOrderDetails().forEach(od -> {
            OrderDetails orderDetails = OrderDetails.builder()
                    .productArticle(od.getProductArticle())
                    .productName(od.getProductName())
                    .productQuantity(od.getProductQuantity())
                    .productPrice(od.getProductPrice())
                    .orderId(byOrderNumberFromDb)
                    .build();
            orderDetailsRepository.save(orderDetails);
        });

        return "Order " + generatedOrderNumber + " created successfully";
    }

    public OrderDto getOrderById(Long id) {
        Order orderFromDb = orderRepository.findById(id);

        List<OrderDetails> allByOrderId = orderDetailsRepository.findAllByOrderId(orderFromDb.getId());

        List<OrderDetailsDto> orderDetailsDtoList = allByOrderId.stream()
                .map(orderDetailsMapper::toOrderDetailsDto)
                .toList();

        return orderMapper.toTDtoWithDetails(orderFromDb, orderDetailsDtoList);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderDto> getOrdersByDateAndMoreThanTotalAmount(LocalDate date, BigDecimal amount) {
        if (date == null || amount == null) {
            throw new RuntimeException("Date and amount must be greater than 0");
        }

        String localDate = String.valueOf(date);

        List<Order> ordersByDateAndMoreThanTotalAmount = orderRepository
                .findOrdersByDateAndMoreThanTotalAmount(localDate, amount);

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
            throw new RuntimeException("Date and amount must be greater than 0");
        }

        String startDate = String.valueOf(start);
        String endDate = String.valueOf(end);

        List<Order> ordersBetweenDatesAndExcludingProduct = orderRepository
                .findOrdersBetweenDatesAndExcludingProduct(productName, startDate, endDate);

        List<OrderDto> orderDtoList = new ArrayList<>();

        ordersBetweenDatesAndExcludingProduct.forEach(order -> {

            List<OrderDetails> orderDetails = orderDetailsRepository
                    .findAllProductsExcludingProductName(productName, order.getId());

            List<OrderDetailsDto> orderDetailsDtoList = orderDetails.stream()
                    .map(orderDetailsMapper::toOrderDetailsDto)
                    .toList();

            OrderDto orderDto = orderMapper.toTDtoWithDetails(order, orderDetailsDtoList);
            orderDtoList.add(orderDto);
        });

        return orderDtoList;
    }

    public void deleteOrder(Long id) {
        if (id == null) {
            throw new RuntimeException("id is null");
        }
        Order byId = orderRepository.findById(id);
        if (byId == null) {
            throw new RuntimeException("Order not found");
        }
        orderDetailsRepository.deleteAllByOrderId(id);
        orderRepository.deleteById(id);
    }

    private static BigDecimal getReduce(OrderDtoForCreateOrder orderDtoForCreateOrder) {
        return orderDtoForCreateOrder.getOrderDetails().stream()
                .map(detail ->
                        BigDecimal.valueOf(detail.getProductPrice())
                                .multiply(BigDecimal.valueOf(detail.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
