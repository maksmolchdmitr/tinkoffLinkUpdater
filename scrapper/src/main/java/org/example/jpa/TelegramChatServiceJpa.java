package org.example.jpa;

import org.example.jpa.entity.User;
import org.example.jpa.repository.UserRepository;
import org.example.service.TelegramChatService;
import org.springframework.transaction.annotation.Transactional;

public class TelegramChatServiceJpa implements TelegramChatService {
    private final UserRepository userRepository;

    public TelegramChatServiceJpa(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void register(long chatId) {
        User user = new User();
        user.setChatId(chatId);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unregister(long chatId) {
        userRepository.deleteById(chatId);
    }
}
