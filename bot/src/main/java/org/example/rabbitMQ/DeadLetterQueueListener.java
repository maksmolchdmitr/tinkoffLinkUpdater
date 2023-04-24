package org.example.rabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.UpdateRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(prefix = "app", name = "rabbitMQConfig.DLQListener", havingValue = "true")
@Component
public class DeadLetterQueueListener {
    @RabbitListener(queues = "${app.rabbitMQConfig.queueName}.dlq")
    public void handleFailedUpdate(UpdateRequest updateRequest){
        log.info("Error occur {}", updateRequest.toString());
    }
}
