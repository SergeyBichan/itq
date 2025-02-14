package testtask.orders.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class OrderDetails {
    private Long id;
    private Long productArticle;
    private String productName;
    private Long productQuantity;
    private Long productPrice;
    private Order orderId;

}
