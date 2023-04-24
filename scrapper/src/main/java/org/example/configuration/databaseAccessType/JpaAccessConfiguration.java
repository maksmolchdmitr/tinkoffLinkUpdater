package org.example.configuration.databaseAccessType;

import org.example.jpa.TelegramChatServiceJpa;
import org.example.jpa.UserLinkServiceJpa;
import org.example.jpa.repository.GithubLinkRepository;
import org.example.jpa.repository.LinkRepository;
import org.example.jpa.repository.UserRepository;
import org.example.service.TelegramChatService;
import org.example.service.UserLinksService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "databaseAccessType", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    TelegramChatService telegramChatService(UserRepository userRepository){
        return new TelegramChatServiceJpa(userRepository);
    }
    @Bean
    UserLinksService userLinksService(LinkRepository linkRepository, UserRepository userRepository, GithubLinkRepository githubLinkRepository){
        return new UserLinkServiceJpa(linkRepository, userRepository, githubLinkRepository);
    }
}
