package testtask.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailsDto {

    @JsonIgnore
    private Long id;
    private Long productArticle;
    private String productName;
    private Long productQuantity;
    private Long productPrice;
    private OrderDto order;

}
