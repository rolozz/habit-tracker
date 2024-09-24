package habittracker.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Главный класс EurekaServerApplication.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    /**
     * Метод для запуска EurekaServerApplication.
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
