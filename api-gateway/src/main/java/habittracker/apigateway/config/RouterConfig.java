package habittracker.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class RouterConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Маршрут для домашней страницы сервиса аутентификации
                .route("auth-service-home", r -> r.path("/home/**")
                        .uri("lb://auth-service"))

                // Проксирование запросов на аутентификацию
                .route("auth-service-login", r -> r.path("/login/**")
                        .uri("lb://auth-service"))

                .route("auth-service-logout", r -> r.path("/logout/**")
                        .uri("lb://auth-service"))

                // Маршрут для защищенной страницы сервиса аутентификации
                .route("auth-service-authenticated", r -> r.path("/authenticated/**")
                        .filters(f -> f.addRequestHeader("X-Authenticated", "true"))
                        .uri("lb://auth-service"))

                // Маршруты для сервиса задач
                .route("task-service-all-tasks", r -> r.path("/tasks/**")
                        .and().method(HttpMethod.GET)
                        .uri("lb://task-service"))

                //Для добавления версионности нужно создать, соответствующие URL-префиксы в ендпоинты контроллеров
//                .route("task-service-v1-all-tasks", r -> r.path("/api/v1/tasks/**")
//                        .and().method(HttpMethod.GET)
//                        .uri("lb://task-service-v1"))
//                .route("task-service-v2-all-tasks", r -> r.path("/api/v2/tasks/**")
//                        .and().method(HttpMethod.GET)
//                        .uri("lb://task-service-v2"))

                .route("task-service-save-task", r -> r.path("/tasks/**")
                        .and().method(HttpMethod.POST)
                        .uri("lb://task-service"))

                .route("task-service-get-task", r -> r.path("/tasks/{id}/**")
                        .and().method(HttpMethod.GET)
                        .uri("lb://task-service"))

                .route("task-service-update-task", r -> r.path("/tasks/{id}/**")
                        .and().method(HttpMethod.PUT)
                        .uri("lb://task-service"))

                .route("task-service-delete-task", r -> r.path("/tasks/{id}/**")
                        .and().method(HttpMethod.DELETE)
                        .uri("lb://task-service"))

                // Маршруты для PomodoroSessionController
                .route("pomodoro-session-start", r -> r.path("/tasks/{id}/pomodoro/start/**")
                        .and().method(HttpMethod.POST)
                        .uri("lb://task-service"))

                .route("pomodoro-session-pause", r -> r.path("/tasks/{id}/pomodoro/pause/**")
                        .and().method(HttpMethod.POST)
                        .uri("lb://task-service"))

                .route("pomodoro-session-resume", r -> r.path("/tasks/{id}/pomodoro/resume/**")
                        .and().method(HttpMethod.POST)
                        .uri("lb://task-service"))

                .route("pomodoro-session-stop", r -> r.path("/tasks/{id}/pomodoro/stop/**")
                        .and().method(HttpMethod.POST)
                        .uri("lb://task-service"))
                .build();
    }
}
