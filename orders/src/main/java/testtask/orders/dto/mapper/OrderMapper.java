package testtask.orders.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.dto.OrderDtoForCreateOrder;
import testtask.orders.dto.OrderDtoWithoutDetails;
import testtask.orders.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

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

    public Order toEntity(OrderDtoForCreateOrder orderDto, LocalDate localDate, String orderNumber, BigDecimal totalAmount) {
        return orderDto == null ? new Order() : Order.builder()
                .orderNumber(orderNumber)
                .totalAmount(totalAmount)
                .orderDate(localDate)
                .orderConsumer(orderDto.getOrderConsumer())
                .deliveryAddress(orderDto.getDeliveryAddress())
                .paymentMethod(orderDto.getPaymentMethod())
                .deliveryMethod(orderDto.getDeliveryMethod())
                .build();
    }

    public OrderDtoWithoutDetails toDtoWithoutDetails(Order order) {
        return order == null ? new OrderDtoWithoutDetails() : OrderDtoWithoutDetails.builder()
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
}
