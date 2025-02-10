package testtask.orders.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetails {

    @Id
    private Long id;
    private Long productArticle;
    private String productName;
    private Long productQuantity;
    private Long productPrice;
    private Long orderId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetails that = (OrderDetails) o;
        return Objects.equals(id, that.id)
                && Objects.equals(productArticle, that.productArticle)
                && Objects.equals(productName, that.productName)
                && Objects.equals(productQuantity, that.productQuantity)
                && Objects.equals(productPrice, that.productPrice)
                && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                productArticle,
                productName,
                productQuantity,
                productPrice,
                orderId
        );
    }
}
