package testtask.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderDtoWithoutDetails {

    @JsonIgnore
    private Long id;
    private String orderNumber;

    @JsonProperty
    private BigDecimal totalOrderPrice;

    @JsonProperty
    private LocalDate orderDate;

    @JsonProperty(required = true)
    private String orderConsumer;

    @JsonProperty(required = true)
    private String deliveryAddress;

    @JsonProperty(required = true)
    private String paymentMethod;

    @JsonProperty(required = true)
    private String deliveryMethod;
}
