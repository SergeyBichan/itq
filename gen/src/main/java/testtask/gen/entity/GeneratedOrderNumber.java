package testtask.gen.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "generated_order_numbers")
@Schema(description = "Сущность сгенерированного номера заказа")
public class GeneratedOrderNumber {

    @Id
    private final String id;
    @Indexed(unique=true)
    private final String orderNumber;
}
