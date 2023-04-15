package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

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
                    Timestamp date
            ){}
        }
    }
    public record Branch(
            String name
    ){}
    public record Event(
            @JsonProperty("created_at")
            Timestamp createdAt
    ){
    }
}
