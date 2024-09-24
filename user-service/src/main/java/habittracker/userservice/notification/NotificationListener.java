package habittracker.userservice.notification;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @KafkaListener(topics = "notificationTopic", groupId = "notification_group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        // Обработка сообщения
    }
}