package org.example.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        @NotNull Scheduler scheduler,
        @NotNull AccessType databaseAccessType,
        @NotNull RabbitMQConfig rabbitMQConfig
) {
    public record Scheduler(Duration interval){}
    public enum AccessType{
        JDBC, JPA, JOOQ
    }
    public record RabbitMQConfig(
            String queueName,
            String exchangeName,
            String routingKey
    ){}
}
