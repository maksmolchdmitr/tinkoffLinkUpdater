package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public record GithubRepositoryResponse(
) {
    public record Event(
            @JsonProperty("created_at")
            Timestamp createdAt
    ){
    }
}
