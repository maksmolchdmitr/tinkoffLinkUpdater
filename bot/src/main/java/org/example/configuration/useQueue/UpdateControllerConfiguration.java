package org.example.configuration.useQueue;

import org.example.controller.UpdateController;
import org.example.service.UpdateHandler;
import org.example.service.UpdateRequestHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "false")
public class UpdateControllerConfiguration {
    @Bean
    UpdateHandler updateHandler(UpdateRequestHandler updateRequestHandler){
        return new UpdateController(updateRequestHandler);
    }
}
