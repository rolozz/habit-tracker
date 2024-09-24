package habittracker.taskservice.kafka.producer;

import habittracker.taskservice.kafka.dto.TaskMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, TaskMessage> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, TaskMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskMessage(String topic, TaskMessage taskMessage) {
        logger.info("Отправка сообщения в топик: {}", topic);

        // Отправляем сообщение и получаем CompletableFuture
        CompletableFuture<SendResult<String, TaskMessage>> future = kafkaTemplate.send(topic, taskMessage);

        // Обрабатываем результат отправки с помощью whenComplete
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                // Логируем ошибку отправки сообщения
                logger.error("Ошибка отправки сообщения в топик: {}", topic, exception);
            } else {
                // Логируем успешную отправку сообщения
                logger.info("Сообщение успешно отправлено в топик: {} с офсетом: {}", topic, result.getRecordMetadata().offset());
            }
        });
    }
}
