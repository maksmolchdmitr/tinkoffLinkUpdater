package org.example.configuration;

import org.example.dto.UpdateRequest;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {
    private final ApplicationConfig appConfig;
    public RabbitMQConfiguration(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Bean
    DirectExchange deadLetterExchange(){
        return new DirectExchange(appConfig.rabbitMQConfig().queueName()+".dlx");
    }
    @Bean
    Queue deadLetterQueue(){
        return new Queue(appConfig.rabbitMQConfig().queueName()+".dlq");
    }
    @Bean
    Binding deadLetterBinding(){
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .withQueueName();
    }

    @Bean
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("org.example.dto.UpdateResponse", UpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("org.example.dto.UpdateRequest");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }
    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }
}
