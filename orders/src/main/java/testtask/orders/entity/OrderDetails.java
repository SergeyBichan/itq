package testtask.orders.entity;

import lombok.*;
import org.springframework.data.annotation.Id;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class OrderDetails {

    @Id
    private Long id;

    private Long product_article;

    private String product_name;

    private Long product_quantity;

    private Long product_price;

    private Order order_id;

}
