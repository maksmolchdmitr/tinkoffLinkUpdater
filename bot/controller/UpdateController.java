package controller;

import dto.UpdateRequest;
import exceptionHandler.ErrorMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    private final List<UpdateRequest> updates = new ArrayList<>();
    @PostMapping
    public void getUpdates(@RequestBody @Valid UpdateRequest updateRequest) {
        updates.add(updateRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleError(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body(
                new ErrorMessage(e.getMessage(),
                        String.valueOf(HttpStatus.BAD_REQUEST),
                        e.getClass().getName(),
                        e.getLocalizedMessage(),
                        Arrays.stream(e.getStackTrace()).map(s -> s.getClassName()+"."+s.getMethodName()+"("+s.getFileName()+")").toList()
                        ));
    }
}
