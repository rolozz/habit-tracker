package habittracker.taskservice.kafka.consumer;

import habittracker.taskservice.kafka.dto.TaskMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "taskTopic", groupId = "task-service-group")
    public void consume(TaskMessage taskMessage) {
        try {
            logger.info("Received message: " + taskMessage);
        } catch (Exception e) {
            logger.error("Error processing message: " + taskMessage, e);
        }
    }
}