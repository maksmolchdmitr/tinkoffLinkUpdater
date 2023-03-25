package org.example;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.configuration.ApplicationConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyTgBot extends TelegramLongPollingBot{
    private static final String NEW_LINK_PRINT_COMMAND = "Print new link";
    private static final String LINK_REMOVE_COMMAND = "Print link you want to remove";
    private static final String HELP_MESSAGE = """
                                /start -- зарегистрировать пользователя
                                /help -- вывести окно с командами
                                /track -- начать отслеживание ссылки
                                /untrack -- прекратить отслеживание ссылки
                                /list -- показать список отслеживаемых ссылок""";
    List<String> links = new ArrayList<>();
    public MyTgBot(ApplicationConfig applicationConfig) {
        super(applicationConfig.botConfig().token());
    }

    @Override
    public void handleUpdate(Update update) {
        if(update.message()!=null&&update.message().text()!=null){
            long chatId = update.message().chat().id();
            bot.execute(new SendChatAction(chatId, ChatAction.typing));
            switch (update.message().text()){
                case "/start" -> handleStart(chatId);
                case "/help" -> handleHelp(chatId);
                case "/track" -> handleTrack(chatId);
                case "/untrack" -> handleUntrack(chatId);
                case "/list" -> handleList(chatId);
                default -> {
                    if(update.message().replyToMessage()!=null){
                        handleRepliedMessage(update, chatId);
                    }else {
                        sendMessage(chatId, "Command was not found!");
                        handleHelp(chatId);
                    }
                }
            }
        }
    }

    private void handleList(long chatId) {
        bot.execute(
                new SendMessage(chatId, "*Links:*\n"
                        +links.stream()
                        .map(s -> "\"`"+s+"`\"")
                        .reduce((s, s2) -> s+"\n"+s2)
                        .orElse("There is not links!"))
                        .disableWebPagePreview(true)
                        .parseMode(ParseMode.Markdown)
        );
    }

    private void handleUntrack(long chatId) {
        bot.execute(new SendMessage(chatId, LINK_REMOVE_COMMAND)
                .replyMarkup(new ForceReply(true))
        );
    }

    private void handleTrack(long chatId) {
        bot.execute(new SendMessage(chatId, NEW_LINK_PRINT_COMMAND)
                .replyMarkup(new ForceReply(true)));
    }

    private void handleHelp(long chatId) {
        sendMessage(chatId, HELP_MESSAGE);
    }

    private void handleStart(long chatId) {
        sendMessage(chatId, "You was registered!");
    }

    private void handleRepliedMessage(Update update, long chatId) {
        switch (update.message().replyToMessage().text()){
            case NEW_LINK_PRINT_COMMAND -> {
                links.add(update.message().text());
                sendMessage(chatId, "Link "+ update.message().text()+" was successfully added!");
            }
            case LINK_REMOVE_COMMAND -> {
                if(links.remove(update.message().text())){
                    sendMessage(chatId, "Link "+ update.message().text()+" was successfully removed!");
                }else {
                    sendMessage(chatId, "Link \""+ update.message().text()+"\" was not found!");
                }
            }
        }
    }
}
