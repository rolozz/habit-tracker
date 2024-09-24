package habittracker.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Operation(summary = "Главная страница")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ")
    })
    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @Operation(summary = "Страница для аутентифицированных пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers() {
        return "Страница защищена";
    }
}

