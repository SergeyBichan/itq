package testtask.orders.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.entity.Order;
import testtask.orders.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsService orderDetailsService;

    public OrderDto findOrderById(Long id) {
        List<OrderDetailsDto> orderDetailsDto = orderDetailsService.getAllOrderDetailsByOrderId(id);

        return orderRepository.findOrderById(id)
                .map(order -> OrderDto.builder()
                        .id(order.getId())
                        .orderNumber(order.getOrder_number())
                        .totalOrderPrice(order.getTotal_amount())
                        .orderDate(order.getOrder_date())
                        .orderConsumer(order.getOrder_consumer())
                        .deliveryAddress(order.getDelivery_address())
                        .paymentMethod(order.getPayment_method())
                        .deliveryMethod(order.getDelivery_method())
                        .orderDetails(orderDetailsDto)
                        .build())
                .orElse(null);
    }

    public List<Order> findAll() {
        List<Order> all = orderRepository.findAll();
        System.out.println(all);
        return all;
    }

    public ResponseEntity<OrderDto> saveOrder(OrderDto orderDto) {
        return null;
    }
}
