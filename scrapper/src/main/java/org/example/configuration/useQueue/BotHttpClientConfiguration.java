package org.example.configuration.useQueue;

import org.example.service.BotHttpClient;
import org.example.service.UpdateSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "false")
public class BotHttpClientConfiguration {
    UpdateSender updateSender(BotHttpClient botHttpClient){
        return botHttpClient::sendUpdates;
    }
}
