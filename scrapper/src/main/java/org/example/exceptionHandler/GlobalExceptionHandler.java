package org.example.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(new ErrorMessage(
                        exception.getMessage(),
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                        exception.getClass().getName(),
                        exception.getLocalizedMessage(),
                        Arrays.stream(exception.getStackTrace())
                                .map(s -> "%s.%s(%s)".formatted(s.getClassName(), s.getMethodName(), s.getFileName()))
                                .toList()
                ));
    }
}
