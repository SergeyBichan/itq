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
                .orderNumber(order.getOrder_number())
                .totalOrderPrice(order.getTotal_amount())
                .orderDate(order.getOrder_date())
                .orderConsumer(order.getOrder_consumer())
                .deliveryAddress(order.getDelivery_address())
                .paymentMethod(order.getPayment_method())
                .deliveryMethod(order.getDelivery_method())
                .orderDetails(orderDetailsDtos)
                .build();
    }

    public OrderWithoutDetailsDto toTDtoWithoutDetails(Order order) {
        return order == null ? new OrderWithoutDetailsDto() : OrderWithoutDetailsDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrder_number())
                .totalOrderPrice(order.getTotal_amount())
                .orderDate(order.getOrder_date())
                .orderConsumer(order.getOrder_consumer())
                .deliveryAddress(order.getDelivery_address())
                .paymentMethod(order.getPayment_method())
                .deliveryMethod(order.getDelivery_method())
                .build();
    }

//    public Order toTEntity(OrderDto orderDto, String generatedOrderNumber){
//        return orderDto == null ? new Order() : Order.builder()
//                .order_number(generatedOrderNumber)
//                .total_amount(orderDto.getTotalOrderPrice())
//                .order_date()
//    }
}
