package testtask.orders.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    private Long id;
    private String orderNumber;
    private BigDecimal totalOrderPrice;
    private LocalDate orderDate;
    private String orderConsumer;
    private String deliveryAddress;
    private String paymentMethod;
    private String deliveryMethod;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderNumber, order.orderNumber)
                && Objects.equals(totalOrderPrice, order.totalOrderPrice)
                && Objects.equals(orderDate, order.orderDate)
                && Objects.equals(orderConsumer, order.orderConsumer)
                && Objects.equals(deliveryAddress, order.deliveryAddress)
                && Objects.equals(paymentMethod, order.paymentMethod)
                && Objects.equals(deliveryMethod, order.deliveryMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, orderNumber,
                totalOrderPrice,
                orderDate,
                orderConsumer,
                deliveryAddress,
                paymentMethod,
                deliveryMethod
        );
    }
}
