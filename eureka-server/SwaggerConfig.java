package business.hub.eurekaserver.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.FileNotFoundException;
import java.util.List;

@Configuration
public class SwaggerConfig {

    public SwaggerConfig() {
    }

    @Bean
    public OpenAPI api() {

        return new OpenAPI()
                .servers(
                        List.of(new Server().url("http://localhost:8761").description("EurekaServer"))
                )
                .openapi("3.0.3")
                .info(
                        new Info().title("Documentation")
        new Info().title("Documentation EurekaServer").version("0.0.1").description("EurekaServer - это платформа для поиска сервисов, разработанная Netflix и широко применяемая в экосистеме микросервисов Spring.\n" +
                "Она предоставляет серверный компонент для регистрации служб и клиентский компонент для обнаружения служб,\n" +
                "позволяя микросервисам самостоятельно регистрироваться и динамически находить другие службы.")
                )
                .tags(
                List.of(new Tag().name("README").description("EurekaServerApplication.\n" +
                        "\n" +
                        "## Конфигурации\n" +
                        "\n" +
                        "Переменные окружения, необходимые для работы сервиса:\n" +
                        "\n" +
                        "- `SwaggerConfig`: Конфигурационный класс для настройки Swagger. (Использует спецификацию OpenAPI)\n" +
                        "- `Application name`: eureka-server\n" +
                        "- `HOSTNAME`: `localhost`\n" +
                        "- `SERVER_PORT`: `8761`\n" +
                        "- `ServiceUrl`: http://localhost:8761/eureka/").externalDocs(new ExternalDocumentation().description("EurekaServer").url("EurekaServer/README.md")))
        );
    }
}

