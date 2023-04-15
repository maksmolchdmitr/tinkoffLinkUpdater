package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowAnswerResponse(
        @JsonProperty("items")
        List<Answer> answers
){
    public record Answer(
            @JsonProperty("last_activity_date")
            OffsetDateTime lastActivityDate
    ){}
}
