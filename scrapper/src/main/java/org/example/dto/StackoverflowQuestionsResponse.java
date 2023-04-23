package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowQuestionsResponse(List<Question> items) {
    public record Question(
            @JsonProperty("last_activity_date")
            OffsetDateTime lastActivityDate
    ){}
}
