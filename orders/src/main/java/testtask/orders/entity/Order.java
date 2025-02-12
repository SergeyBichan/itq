package testtask.orders.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Order {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private String orderConsumer;
    private String deliveryAddress;
    private String paymentMethod;
    private String deliveryMethod;
}
