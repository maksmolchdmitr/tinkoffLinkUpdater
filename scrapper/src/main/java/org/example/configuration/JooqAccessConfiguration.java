package org.example.configuration;

import org.example.jooq.TelegramChatServiceJooq;
import org.example.jooq.UserLinksServiceJooq;
import org.example.service.TelegramChatService;
import org.example.service.UserLinksService;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "databaseAccessType", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    TelegramChatService telegramChatService(DSLContext dsl){
        return new TelegramChatServiceJooq(dsl);
    }
    @Bean
    UserLinksService userLinksService(DSLContext dsl){
        return new UserLinksServiceJooq(dsl);
    }
}
