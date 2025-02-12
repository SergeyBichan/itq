package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testtask.orders.constants.Constants;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.dto.mapper.OrderMapper;
import testtask.orders.entity.Order;
import testtask.orders.entity.OrderDetails;
import testtask.orders.repository.impl.OrderDetailsRepository;
import testtask.orders.repository.impl.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public Order getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public void createOrderDetails(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }

    public OrderDetails getOrderDetailsById(Long id) {
        return orderDetailsRepository.findById(id);
    }

    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailsRepository.findAll();
    }

    public void deleteOrderDetails(Long id) {
        orderDetailsRepository.deleteById(id);
    }

    private static BigDecimal getReduce(OrderDtoForCreateOrder orderDtoForCreateOrder) {
        return orderDtoForCreateOrder.getOrderDetails().stream()
                .map(detail ->
                        BigDecimal.valueOf(detail.getProductPrice())
                                .multiply(BigDecimal.valueOf(detail.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
