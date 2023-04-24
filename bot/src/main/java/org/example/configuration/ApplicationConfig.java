package org.example.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        @NotNull BotConfig botConfig,
        @NotNull RabbitMQConfig rabbitMQConfig,
        @NotNull boolean useQueue
) {
    public record BotConfig(String name, String token){}
    public record RabbitMQConfig(String queueName){}
}
