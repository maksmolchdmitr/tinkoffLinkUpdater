package org.example.jpa;

import jakarta.transaction.Transactional;
import org.example.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class TelegramChatServiceJpaTest extends IntegrationEnvironment {
    @Autowired
    private TelegramChatServiceJpa telegramChatServiceJpa;

    @Test
    @Transactional
    @Rollback
    void register() {
        telegramChatServiceJpa.register(0);
    }

    @Test
    @Transactional
    @Rollback
    void unregister() {
        telegramChatServiceJpa.register(0);
        telegramChatServiceJpa.unregister(0);
    }
}