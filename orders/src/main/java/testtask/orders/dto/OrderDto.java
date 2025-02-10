package testtask.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDto {

    @JsonIgnore
    private Long id;
    private String orderNumber;
    private BigDecimal totalOrderPrice;
    private LocalDate orderDate;
    private String orderConsumer;
    private String deliveryAddress;
    private String paymentMethod;
    private String deliveryMethod;
    private List<OrderDetailsDto> orderDetails;

}
