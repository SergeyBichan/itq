package testtask.orders.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderWithoutDetailsDto {
    private Long id;
    private String orderNumber;
    private BigDecimal totalOrderPrice;
    private LocalDate orderDate;
    private String orderConsumer;
    private String deliveryAddress;
    private String paymentMethod;
    private String deliveryMethod;
}
