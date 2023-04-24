package org.example.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    private final ApplicationConfig appConfig;
    public RabbitMQConfiguration(ApplicationConfig applicationConfig) {
        this.appConfig = applicationConfig;
    }

    @Bean
    Queue queue(){
        return QueueBuilder
                .durable(appConfig.rabbitMQConfig().queueName())
                .withArgument("x-dead-letter-exchange", appConfig.rabbitMQConfig().queueName()+".dlx")
                .withArgument("x-dead-letter-routing-key", appConfig.rabbitMQConfig().queueName()+".dlq")
                .build();
    }
    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(appConfig.rabbitMQConfig().exchangeName(), true, false);
    }
    @Bean
    Binding binding(Queue queue, DirectExchange directExchange){
        return BindingBuilder
                .bind(queue)
                .to(directExchange)
                .with(appConfig.rabbitMQConfig().routingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
