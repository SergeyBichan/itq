package testtask.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private String productName;
    @JsonProperty(required = true)
    private Long productQuantity;
    @JsonProperty(required = true)
    private Long productPrice;
}
