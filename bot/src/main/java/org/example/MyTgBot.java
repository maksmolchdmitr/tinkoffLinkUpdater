package org.example;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.configuration.ApplicationConfig;
import org.example.dto.AddLinkRequest;
import org.example.dto.RemoveLinkRequest;
import org.example.dto.UpdateRequest;
import org.example.service.ScrapperClient;
import org.example.service.UpdateRequestHandler;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class MyTgBot extends TelegramLongPollingBot implements UpdateRequestHandler {
    private static final String NEW_LINK_PRINT_COMMAND = "Print new link";
    private static final String LINK_REMOVE_COMMAND = "Print link you want to remove";
    private static final String HELP_MESSAGE = """
                                /start -- зарегистрировать пользователя
                                /help -- вывести окно с командами
                                /track -- начать отслеживание ссылки
                                /untrack -- прекратить отслеживание ссылки
                                /list -- показать список отслеживаемых ссылок""";
    private static final String NOT_LINK_ERROR_MESSAGE = "This is not url link!";
    private final ScrapperClient scrapperClient;
    public MyTgBot(ApplicationConfig applicationConfig, ScrapperClient scrapperClient) {
        super(applicationConfig.botConfig().token());
        this.scrapperClient = scrapperClient;
    }

    @Override
    public void handleUpdate(Update update) {
        if(update.message()!=null&&update.message().text()!=null){
            long chatId = update.message().chat().id();
            bot.execute(new SendChatAction(chatId, ChatAction.typing));
            handleUpdateMessageTextCommand(update, chatId);
        }
    }

    protected void handleUpdateMessageTextCommand(Update update, long chatId) {
        switch (update.message().text()){
            case "/start" -> handleStart(chatId);
            case "/help" -> sendHelpText(chatId);
            case "/track" -> handleTrack(chatId);
            case "/untrack" -> handleUntrack(chatId);
            case "/list" -> handleList(chatId);
            default -> handleUnknownCommand(update, chatId);
        }
    }

    protected void handleUnknownCommand(Update update, long chatId) {
        if(update.message().replyToMessage()!=null){
            handleRepliedMessage(update, chatId);
        }else {
            sendMessage(chatId, "Command was not found!");
            sendHelpText(chatId);
        }
    }

    protected void handleList(long chatId) {
        bot.execute(
                new SendMessage(chatId, "*Links:*\n"
                        +scrapperClient.getLinks(chatId).links().stream()
                        .map(s -> "\"`"+s.url()+"`\"")
                        .reduce((s, s2) -> s+"\n"+s2)
                        .orElse("There is not links!"))
                        .disableWebPagePreview(true)
                        .parseMode(ParseMode.Markdown)
        );
    }

    protected void handleUntrack(long chatId) {
        bot.execute(new SendMessage(chatId, LINK_REMOVE_COMMAND)
                .replyMarkup(new ForceReply(true))
        );
    }

    protected void handleTrack(long chatId) {
        bot.execute(new SendMessage(chatId, NEW_LINK_PRINT_COMMAND)
                .replyMarkup(new ForceReply(true)));
    }

    protected void sendHelpText(long chatId) {
        sendMessage(chatId, HELP_MESSAGE);
    }

    protected void handleStart(long chatId) {
        scrapperClient.registerUser(chatId);
        sendMessage(chatId, "You was registered!");
    }

    protected void handleRepliedMessage(Update update, long chatId) {
        switch (update.message().replyToMessage().text()){
            case NEW_LINK_PRINT_COMMAND -> tryToAddLink(update, chatId);
            case LINK_REMOVE_COMMAND -> tryToRemoveLink(update, chatId);
        }
    }

    private void tryToRemoveLink(Update update, long chatId) {
        try {
            removeLink(update, chatId);
        } catch (MalformedURLException e) {
            sendMessage(chatId, NOT_LINK_ERROR_MESSAGE);
        }
    }

    private void removeLink(Update update, long chatId) throws MalformedURLException {
        if(scrapperClient.deleteLink(chatId, new RemoveLinkRequest(new URL(update.message().text())))!=null) {
            sendMessage(chatId, "Link " + update.message().text() + " was successfully removed!");
        }else {
            sendMessage(chatId, "Link " + update.message().text() + " was not found or you haven't been registered!");
            sendHelpText(chatId);
        }
    }

    private void tryToAddLink(Update update, long chatId) {
        try {
            addLink(update, chatId);
        } catch (MalformedURLException e) {
            sendMessage(chatId, NOT_LINK_ERROR_MESSAGE);
        }
    }

    private void addLink(Update update, long chatId) throws MalformedURLException {
        if(scrapperClient.addLink(chatId, new AddLinkRequest(new URL(update.message().text())))!=null) {
            sendMessage(chatId, "Link " + update.message().text() + " was successfully added!");
        }else {
            sendMessage(chatId, "You haven't been registered!");
            sendHelpText(chatId);
        }
    }

    @Override
    public void handleUpdateRequest(UpdateRequest updateRequest) {
        for(Long tgChatId:updateRequest.tgChatIds()){
            sendMessage(tgChatId,
                    String.format("""
                    Link:
                    %s
                    was detected as updated!!!
                    With description:
                    %s
                    """, updateRequest.url(), updateRequest.description()));
        }
    }
}
