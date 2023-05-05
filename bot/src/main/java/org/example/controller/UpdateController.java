package org.example.controller;

import org.example.dto.UpdateRequest;
import org.example.exceptionHandler.ErrorMessage;
import jakarta.validation.Valid;
import org.example.service.UpdateHandler;
import org.example.service.UpdateRequestHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RequestMapping("/updates")
public class UpdateController implements UpdateHandler {
    private final UpdateRequestHandler updateRequestHandler;

    public UpdateController(UpdateRequestHandler updateRequestHandler) {
        this.updateRequestHandler = updateRequestHandler;
    }

    @PostMapping
    @Override
    public void getUpdates(@RequestBody @Valid UpdateRequest updateRequest) {
        updateRequestHandler.handleUpdateRequest(updateRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleError(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                new ErrorMessage(e.getMessage(),
                        String.valueOf(HttpStatus.BAD_REQUEST),
                        e.getClass().getName(),
                        e.getLocalizedMessage(),
                        Arrays.stream(e.getStackTrace())
                                .map(s -> "%s.%s(%s)"
                                        .formatted(s.getClassName(), s.getMethodName(), s.getFileName()))
                                .toList()
                ));
    }
}
