package testtask.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonPropertyOrder
public class OrderDto {

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
    @JsonProperty(required = true)
    private List<OrderDetailsDto> orderDetails;

}
