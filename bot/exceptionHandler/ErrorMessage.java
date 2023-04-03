package exceptionHandler;

import java.util.List;

public record ErrorMessage(String description,
                           String code,
                           String exceptionName,
                           String exceptionMessage,
                           List<String> stacktrace
){}