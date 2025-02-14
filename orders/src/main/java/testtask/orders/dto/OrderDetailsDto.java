package testtask.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailsDto {

    @NotNull(message = "ID не может быть null")
    private Long id;

    @NotNull(message = "Артикул товара не может быть null")
    @Min(value = 1, message = "Артикул должен быть положительным числом")
    private Long productArticle;

    @NotBlank(message = "Название товара не может быть пустым")
    @Size(max = 255, message = "Название товара не должно превышать 255 символов")
    private String productName;

    @NotNull(message = "Количество не может быть null")
    @Min(value = 1, message = "Количество должно быть положительным числом")
    private Long productQuantity;

    @NotNull(message = "Цена не может быть null")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Long productPrice;
}
