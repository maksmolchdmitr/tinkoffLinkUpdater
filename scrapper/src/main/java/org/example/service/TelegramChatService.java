package org.example.service;

public interface TelegramChatService {
    void register(long chatId);
    void unregister(long chatId);
}
