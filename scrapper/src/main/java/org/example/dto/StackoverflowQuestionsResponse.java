package org.example.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowQuestionsResponse(List<Question> items) {
    public record Question(OffsetDateTime last_activity_date){}
}
