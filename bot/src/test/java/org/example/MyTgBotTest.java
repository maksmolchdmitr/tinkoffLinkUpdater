package org.example;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@SpringBootTest
class MyTgBotTest {
    @Autowired
    private MyTgBot myTgBot;
    @Mock
    private Chat chat;
    @Mock
    private Message message;
    @Mock
    private Update update;
    private static final long chatId = 31204213;

    @BeforeEach
    void spying() {
        myTgBot = spy(myTgBot);
        myTgBot.bot = spy(myTgBot.bot);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
    }

    @Test
    void handleUpdate_inputCommandList_shouldReturnEmptyListText() {
        //given
        final String text = "/list";
        when(message.text()).thenReturn(text);
        //when
        final String emptyListResponseText = "*Links:*\n" + "There is not links!";
        final ArgumentMatcher<BaseRequest> emptyListSendMessageTextMatcher =
                baseRequest -> {
                    if(baseRequest.getParameters()!=null) {
                        if(baseRequest.getParameters().containsKey("text")) {
                            return baseRequest.getParameters().get("text").equals(emptyListResponseText);
                        }
                    }
                    return false;
                };
        myTgBot.handleUpdate(update);
        //then
        assertAll(
                () -> verify(myTgBot).handleUpdate(update),
                () -> verify(myTgBot).handleList(chatId),
                () -> verify(myTgBot.bot).execute(any(SendMessage.class)),
                () -> verify(myTgBot.bot).execute(argThat(emptyListSendMessageTextMatcher))
        );
    }

    @Test
    void handleUpdate_inputCommandListWithOneCountLinkList_shouldReturnOneCountList(){
        //given
        final String text = "/list";
        when(message.text()).thenReturn(text);
        myTgBot.links = List.of("https://stackoverflow.com/questions/14292863/how-to-mock-a-final-class-with-mockito");
        //when
        final String notEmptyListTextResponse = "*Links:*\n"+"\"`https://stackoverflow.com/questions/14292863/how-to-mock-a-final-class-with-mockito`\"";
        final ArgumentMatcher<BaseRequest> notEmptyListSendMessageTextMatcher =
                baseRequest -> {
                    if(baseRequest.getParameters()!=null) {
                        if(baseRequest.getParameters().containsKey("text")) {
                            return baseRequest.getParameters().get("text").equals(notEmptyListTextResponse);
                        }
                    }
                    return false;
                };
        myTgBot.handleUpdate(update);
        //then
        assertAll(
                () -> verify(myTgBot).handleUpdate(update),
                () -> verify(myTgBot).handleList(chatId),
                () -> verify(myTgBot.bot).execute(any(SendMessage.class)),
                () -> verify(myTgBot.bot).execute(argThat(notEmptyListSendMessageTextMatcher))
        );
    }

    @Test
    void handleUpdate_inputCommandListWithNotEmptyLinkList_shouldReturnNotEmptyList(){
        //given
        final String text = "/list";
        when(message.text()).thenReturn(text);
        myTgBot.links = List.of("https://stackoverflow.com/questions/14292863/how-to-mock-a-final-class-with-mockito",
                "https://github.com/maksmolchdmitr/tinkoffLinkUpdater");
        //when
        final String notEmptyListTextResponse = """
                *Links:*
                "`https://stackoverflow.com/questions/14292863/how-to-mock-a-final-class-with-mockito`"
                "`https://github.com/maksmolchdmitr/tinkoffLinkUpdater`\"""";
        final ArgumentMatcher<BaseRequest> notEmptyListSendMessageTextMatcher =
                baseRequest -> {
                    if(baseRequest.getParameters()!=null) {
                        if(baseRequest.getParameters().containsKey("text")) {
                            return baseRequest.getParameters().get("text").equals(notEmptyListTextResponse);
                        }
                    }
                    return false;
                };
        myTgBot.handleUpdate(update);
        //then
        assertAll(
                () -> verify(myTgBot).handleUpdate(update),
                () -> verify(myTgBot).handleList(chatId),
                () -> verify(myTgBot.bot).execute(any(SendMessage.class)),
                () -> verify(myTgBot.bot).execute(argThat(notEmptyListSendMessageTextMatcher))
        );
    }

}