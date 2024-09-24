package habittracker.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Основной класс TaskServiceApplication.
 */
@SpringBootApplication
@EnableCaching
public class TaskServiceApplication {

    /**
     * Метод для запуска TaskServiceApplication.
     * @param args - _
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }

}
