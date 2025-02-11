package testtask.orders.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import testtask.orders.dto.OrderDetailsDto;
import testtask.orders.dto.OrderDto;
import testtask.orders.entity.Order;
import testtask.orders.service.OrderDetailsService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderDetailsService orderDetailsService;

    public OrderDto toTDto(Order order, List<OrderDetailsDto> orderDetailsDtos) {

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
}
