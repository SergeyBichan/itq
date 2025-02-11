package testtask.gen.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Generate Order Number System Api",
                description = "API системы генерации номера заказа",
                version = "1.0.0",
                contact = @Contact(
                        name = "Bichan Sergey",
                        email = "bichan.sergey@gmail.com"
                )
        )
)
public class OpenApiConfig {

}
