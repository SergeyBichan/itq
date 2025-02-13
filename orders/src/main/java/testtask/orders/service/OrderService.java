package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static testtask.orders.constants.Constants.URI_FOR_GENERATE_NUMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final RestTemplate restTemplate;
    private final OrderMapper orderMapper;
    private final OrderDetailsMapper orderDetailsMapper;


    public void createOrder(OrderDtoForCreateOrder orderDtoForCreateOrder) {

        String generatedOrderNumber = restTemplate.getForObject(URI_FOR_GENERATE_NUMBER, String.class);
        String dateNow = generatedOrderNumber.substring(5);
        LocalDate date = LocalDate.parse(dateNow, DateTimeFormatter.ofPattern("yyyyMMdd"));

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
    }

    public OrderDto getOrderById(Long id) {
        if (id <= 0) {
            throw new RuntimeException("Order id must be greater than 0");
        }
        Order orderFromDb = orderRepository.findById(id);

        List<OrderDetails> allByOrderId = orderDetailsRepository.findAllByOrderId(orderFromDb.getId());

        List<OrderDetailsDto> orderDetailsDtoList = allByOrderId.stream()
                .map(orderDetailsMapper::toOrderDetailsDto)
                .toList();

        OrderDto orderDto = orderMapper.toTDtoWithDetails(orderFromDb, orderDetailsDtoList);


        return orderDto;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderDto> getAllOrdersByDateAndMoreThanTotalAmount(LocalDate date, BigDecimal amount) {
        if (date == null || amount == null) {
            throw new RuntimeException("Date and amount must be greater than 0");
        }

        String localDate = String.valueOf(date);

        List<Order> ordersByDateAndMoreThanTotalAmount = orderRepository
                .findOrdersByDateAndMoreThanTotalAmount(localDate, amount);
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        ordersByDateAndMoreThanTotalAmount.forEach(order -> {
            orderDetailsList.addAll(orderDetailsRepository.findAllByOrderId(order.getId()));
        });

        List<OrderDetailsDto> orderDetailsDtoList = orderDetailsList.stream()
                .map(orderDetailsMapper::toOrderDetailsDto)
                .toList();

        return ordersByDateAndMoreThanTotalAmount.stream()
                .map(el -> orderMapper.toTDtoWithDetails(el, orderDetailsDtoList))
                .toList();
    }

    public void deleteOrder(Long id) {
        if (id == null) {
            throw new RuntimeException("id is null");
        }
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
