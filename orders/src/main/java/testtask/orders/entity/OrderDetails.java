package testtask.orders.entity;

import lombok.*;

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
