package org.example.service;

import org.example.configuration.ApplicationConfig;
import org.example.dto.UpdateResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScrapperQueueProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationConfig appConfig;
    public ScrapperQueueProducer(RabbitTemplate rabbitTemplate, ApplicationConfig appConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.appConfig = appConfig;
    }

    public void send(UpdateResponse updateResponse){
        rabbitTemplate.convertAndSend(appConfig.rabbitMQConfig().routingKey(), updateResponse);
    }
}
