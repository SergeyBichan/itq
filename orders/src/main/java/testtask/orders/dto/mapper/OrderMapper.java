package testtask.orders.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderWithoutDetailsDto;
import testtask.orders.entity.Order;
import testtask.orders.service.OrderDetailsService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderDetailsService orderDetailsService;

    public OrderDto toTDtoWithDetails(Order order, List<OrderDetailsDto> orderDetailsDtos) {
        return order == null ? new OrderDto() : OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalOrderPrice(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .orderConsumer(order.getOrderConsumer())
                .deliveryAddress(order.getDeliveryAddress())
                .paymentMethod(order.getPaymentMethod())
                .deliveryMethod(order.getDeliveryMethod())
                .orderDetails(orderDetailsDtos)
                .build();
    }

    public OrderWithoutDetailsDto toTDtoWithoutDetails(Order order) {
        return order == null ? new OrderWithoutDetailsDto() : OrderWithoutDetailsDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalOrderPrice(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .orderConsumer(order.getOrderConsumer())
                .deliveryAddress(order.getDeliveryAddress())
                .paymentMethod(order.getPaymentMethod())
                .deliveryMethod(order.getDeliveryMethod())
                .build();
    }

    public Order toEntity(OrderDto orderDto) {
        return orderDto == null ? new Order() : Order.builder()
                .orderNumber(orderDto.getOrderNumber())
                .totalAmount(orderDto.getTotalOrderPrice())
                .orderDate(orderDto.getOrderDate())
                .orderConsumer(orderDto.getOrderConsumer())
                .deliveryAddress(orderDto.getDeliveryAddress())
                .paymentMethod(orderDto.getPaymentMethod())
                .deliveryMethod(orderDto.getDeliveryMethod())
                .build();
    }
}
