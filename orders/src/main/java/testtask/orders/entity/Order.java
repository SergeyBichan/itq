package testtask.orders.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Order {

    @Id
    private Long id;

    private String order_number;

    private BigDecimal total_amount;

    private LocalDate order_date;

    private String order_consumer;

    private String delivery_address;

    private String payment_method;

    private String delivery_method;


}
