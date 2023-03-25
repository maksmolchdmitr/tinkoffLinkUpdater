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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/links")
public class LinksController {
    private final Map<String, Long> links = new HashMap<>();
    private long counter = 0L;
    @GetMapping
    public ListLinksResponse sendTrackedLinks(){
        return new ListLinksResponse(
                links.entrySet().stream().map(e -> new LinkResponse(e.getValue(), e.getKey())).toList(),
                links.size()
        );
    }

    @PostMapping
    public void addLinkTracking(@RequestBody AddLinkRequest addLinkRequest){
        if(links.containsKey(addLinkRequest.link())){
            throw new IllegalArgumentException("This link with url "+addLinkRequest.link()+" already exist!");
        }
        links.put(addLinkRequest.link(), counter++);
    }

    @DeleteMapping
    public LinkResponse deleteLinkTracking(@RequestBody RemoveLinkRequest removeLinkRequest){
        Long id = links.remove(removeLinkRequest.link());
        if(id==null){
            throw new IllegalArgumentException("Link with url "+removeLinkRequest.link()+" doesn't exist!");
        }
        return new LinkResponse(id, removeLinkRequest.link());
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
