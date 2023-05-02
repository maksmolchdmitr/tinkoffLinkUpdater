package org.example.controller;

import org.example.dto.AddLinkRequest;
import org.example.dto.LinkResponse;
import org.example.dto.ListLinksResponse;
import org.example.dto.RemoveLinkRequest;
import org.example.exceptionHandler.ErrorMessage;
import org.example.model.UserLinks;
import org.example.service.UserLinksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/links")
public class LinksController {
    private final static Function<StackTraceElement, String> STACK_TRACE_TO_STRING =
            s -> "%s.%s(%s)".formatted(s.getClassName(), s.getMethodName(), s.getFileName());
    private final UserLinksService userLinksService;

    public LinksController(UserLinksService userLinksService) {
        this.userLinksService = userLinksService;
    }

    @GetMapping
    public ListLinksResponse sendTrackedLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        List<LinkResponse> res = userLinksService.findByChatId(tgChatId).stream()
                .map(userLinks -> new LinkResponse(userLinks.userChatId(), userLinks.linkUrl()))
                .toList();
        return new ListLinksResponse(res, res.size());
    }

    @PostMapping
    public LinkResponse addLinkTracking(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        UserLinks userLinks = userLinksService.add(tgChatId, String.valueOf(addLinkRequest.link()));
        if (userLinks == null) return null;
        return new LinkResponse(userLinks.userChatId(), userLinks.linkUrl());
    }

    @DeleteMapping
    public LinkResponse deleteLinkTracking(
            @RequestHeader("Tg-Chat-Id") Long tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        UserLinks removedUserLinks = userLinksService.remove(tgChatId, String.valueOf(removeLinkRequest.link()));
        if (removedUserLinks == null) return null;
        return new LinkResponse(removedUserLinks.userChatId(), removedUserLinks.linkUrl());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidException(Exception e) {
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
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
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
