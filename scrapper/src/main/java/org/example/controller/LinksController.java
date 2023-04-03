package org.example.controller;

import org.example.dto.AddLinkRequest;
import org.example.dto.LinkResponse;
import org.example.dto.ListLinksResponse;
import org.example.dto.RemoveLinkRequest;
import org.example.exceptionHandler.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/links")
public class LinksController {
    private final Map<Long, Set<String>> userLinks = new HashMap<>();
    @GetMapping
    public ListLinksResponse sendTrackedLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId){
        if(!userLinks.containsKey(tgChatId)){
            userLinks.put(tgChatId, new HashSet<>());
        }
        return new ListLinksResponse(
                userLinks.get(tgChatId).stream().map(s -> new LinkResponse(0L, s)).toList(),
                userLinks.get(tgChatId).size()
        );
    }

    @PostMapping
    public void addLinkTracking(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody AddLinkRequest addLinkRequest){
        if(!userLinks.containsKey(tgChatId)){
            userLinks.put(tgChatId, new HashSet<>());
        }
        if(userLinks.get(tgChatId).contains(addLinkRequest.link())){
            throw new IllegalArgumentException("This link with url "+addLinkRequest.link()+" already exist!");
        }
        userLinks.get(tgChatId).add(addLinkRequest.link());
    }

    @DeleteMapping
    public LinkResponse deleteLinkTracking(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest){
        if(!userLinks.containsKey(tgChatId)){
            userLinks.put(tgChatId, new HashSet<>());
        }
        if(!userLinks.get(tgChatId).remove(removeLinkRequest.link())){
            throw new IllegalArgumentException("Link with url "+removeLinkRequest.link()+" doesn't exist!");
        }
        return new LinkResponse(0L, removeLinkRequest.link());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidException(Exception e){
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
    public ResponseEntity<ErrorMessage> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
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
