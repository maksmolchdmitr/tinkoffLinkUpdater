package org.example.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
        return new Queue(appConfig.rabbitMQConfig().queueName());
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
