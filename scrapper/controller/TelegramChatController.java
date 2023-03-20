package controller;

import exceptionHandler.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tg-chat")
public class TelegramChatController {
    private final Set<Long> chatIds = new HashSet<>();
    @PostMapping("{id}")
    public void registerChat(@PathVariable Long id){
        chatIds.add(id);
    }

    @DeleteMapping("{id}")
    public void deleteChat(@PathVariable Long id){
        if(!chatIds.remove(id)){
            throw new IllegalArgumentException("Chat with id = "+id+" is not exist!");
        }
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
