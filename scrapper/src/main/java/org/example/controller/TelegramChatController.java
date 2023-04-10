package org.example.controller;

import org.example.exceptionHandler.ErrorMessage;
import org.example.jdbc.TelegramChatServiceJdbc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tg-chat")
public class TelegramChatController {
    private final TelegramChatServiceJdbc telegramChatServiceJdbc;

    public TelegramChatController(TelegramChatServiceJdbc telegramChatServiceJdbc) {
        this.telegramChatServiceJdbc = telegramChatServiceJdbc;
    }

    @PostMapping("{id}")
    public void registerChat(@PathVariable Long id){
        telegramChatServiceJdbc.register(id);
    }

    @DeleteMapping("{id}")
    public void deleteChat(@PathVariable Long id){
        telegramChatServiceJdbc.unregister(id);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidException(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(
                new ErrorMessage(
                        e.getMessage(),
                        String.valueOf(HttpStatus.BAD_REQUEST),
                        e.getClass().getName(),
                        e.getLocalizedMessage(),
                        Arrays.stream(e.getStackTrace()).map(s -> s.getClassName()+"."+s.getMethodName()+"("+s.getFileName()+")").toList()
                )
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleException(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorMessage(
                                e.getMessage(),
                                String.valueOf(HttpStatus.NOT_FOUND),
                                e.getClass().getName(),
                                e.getLocalizedMessage(),
                                Arrays.stream(e.getStackTrace()).map(s -> s.getClassName()+"."+s.getMethodName()+"("+s.getFileName()+")").toList()
                        )
                );
    }

}
