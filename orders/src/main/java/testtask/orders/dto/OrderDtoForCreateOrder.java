package testtask.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDtoForCreateOrder {

    @NotBlank(message = "Покупатель не может быть пустым")
    private String orderConsumer;

    @NotBlank(message = "Адрес доставки не может быть пустым")
    private String deliveryAddress;

    @NotBlank(message = "Метод оплаты не может быть пустым")
    private String paymentMethod;

    @NotBlank(message = "Метод доставки не может быть пустым")
    private String deliveryMethod;

    @NotNull(message = "Детали заказа не могут быть null")
    @Size(min = 1, message = "Заказ должен содержать хотя бы один товар")
    private List<OrderDetailsDto> orderDetails;
}
