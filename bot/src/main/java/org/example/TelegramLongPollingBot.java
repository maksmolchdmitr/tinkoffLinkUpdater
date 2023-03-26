package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class TelegramLongPollingBot implements UpdateHandler {
    protected TelegramBot bot;

    public TelegramLongPollingBot(String token) {
        this.bot = new TelegramBot(token);
        this.bot.setUpdatesListener(updates -> {
            updates.forEach(this::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void sendMessage(Long chatId, String message){
        this.bot.execute(new SendMessage(chatId, message));
    }

}
