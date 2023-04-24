package org.example.rabbitMQ;

import org.example.configuration.ApplicationConfig;
import org.example.dto.UpdateResponse;
import org.example.service.UpdateSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScrapperQueueProducer implements UpdateSender {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationConfig appConfig;
    public ScrapperQueueProducer(RabbitTemplate rabbitTemplate, ApplicationConfig appConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.appConfig = appConfig;
    }

    @Override
    public void sendUpdates(UpdateResponse updateResponse){
        rabbitTemplate.convertAndSend(appConfig.rabbitMQConfig().routingKey(), updateResponse);
    }
}
