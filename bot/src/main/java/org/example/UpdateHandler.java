package org.example;

import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {
    void handleUpdate(Update update);
}
