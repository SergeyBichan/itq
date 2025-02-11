package testtask.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailsDto {

    @JsonIgnore
    private Long id;
    private Long productArticle;
    private String productName;
    private Long productQuantity;
    private Long productPrice;

}
