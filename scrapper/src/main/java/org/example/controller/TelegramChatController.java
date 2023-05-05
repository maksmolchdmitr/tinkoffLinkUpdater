package org.example.controller;

import org.example.exceptionHandler.ErrorMessage;
import org.example.service.TelegramChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.function.Function;

@RestController
@RequestMapping("/tg-chat")
public class TelegramChatController {
    private final static Function<StackTraceElement, String> STACK_TRACE_TO_STRING =
            s -> "%s.%s(%s)".formatted(s.getClassName(), s.getMethodName(), s.getFileName());
    private final TelegramChatService telegramChatService;

    public TelegramChatController(TelegramChatService telegramChatService) {
        this.telegramChatService = telegramChatService;
    }

    @PostMapping("{id}")
    public void registerChat(@PathVariable Long id) {
        telegramChatService.register(id);
    }

    @DeleteMapping("{id}")
    public void deleteChat(@PathVariable Long id) {
        telegramChatService.unregister(id);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                new ErrorMessage(
                        e.getMessage(),
                        String.valueOf(HttpStatus.BAD_REQUEST),
                        e.getClass().getName(),
                        e.getLocalizedMessage(),
                        Arrays.stream(e.getStackTrace())
                                .map(STACK_TRACE_TO_STRING)
                                .toList()
                )
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorMessage(
                                e.getMessage(),
                                String.valueOf(HttpStatus.NOT_FOUND),
                                e.getClass().getName(),
                                e.getLocalizedMessage(),
                                Arrays.stream(e.getStackTrace())
                                        .map(STACK_TRACE_TO_STRING)
                                        .toList()
                        )
                );
    }

}
