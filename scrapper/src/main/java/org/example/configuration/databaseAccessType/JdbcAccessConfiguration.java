package org.example.configuration.databaseAccessType;

import org.example.dao.GithubLinkDao;
import org.example.dao.LinkDao;
import org.example.dao.UserDao;
import org.example.dao.UserLinksDao;
import org.example.jdbc.TelegramChatServiceJdbc;
import org.example.jdbc.UserLinksServiceJdbc;
import org.example.service.TelegramChatService;
import org.example.service.UserLinksService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "databaseAccessType", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    TelegramChatService telegramChatService(UserDao userDao, UserLinksDao userLinksDao, LinkDao linkDao){
        return new TelegramChatServiceJdbc(userDao, userLinksDao, linkDao);
    }
    @Bean
    UserLinksService userLinksService(GithubLinkDao githubLinkDao, UserLinksDao userLinksDao, LinkDao linkDao){
        return new UserLinksServiceJdbc(userLinksDao, linkDao, githubLinkDao);
    }
}
