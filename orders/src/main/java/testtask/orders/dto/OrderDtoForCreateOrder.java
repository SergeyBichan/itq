package testtask.orders.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDtoForCreateOrder {

    private String orderConsumer;
    private String deliveryAddress;
    private String paymentMethod;
    private String deliveryMethod;
    private List<OrderDetailsDto> orderDetails;
}
