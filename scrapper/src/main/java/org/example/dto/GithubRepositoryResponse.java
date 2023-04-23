package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record GithubRepositoryResponse(
) {
    public record CommitBody(
            Commit commit
    ){
        public record Commit(
                Author author,
                String message
        ){
            public record Author(
                    String name,
                    ZonedDateTime date
            ){}
        }
    }
    public record Branch(
            String name
    ){}
    public record Event(
            @JsonProperty("created_at")
            ZonedDateTime createdAt
    ){
        @Override
        public String toString() {
            return "Event{" +
                    "createdAt=" + createdAt +
                    '}';
        }
    }
}
