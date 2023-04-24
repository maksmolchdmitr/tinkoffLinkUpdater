package org.example.configuration;

import org.example.rabbitMQ.ScrapperQueueProducer;
import org.example.service.UpdateSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "true")
public class QueueProducerConfiguration {
    UpdateSender updateSender(RabbitTemplate rabbitTemplate, ApplicationConfig appConfig){
        return new ScrapperQueueProducer(rabbitTemplate, appConfig);
    }
}
