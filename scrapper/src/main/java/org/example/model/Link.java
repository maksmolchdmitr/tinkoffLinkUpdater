package org.example.model;

import java.sql.Timestamp;

public record Link(
        String url,
        Timestamp lastUpdate,
        Boolean isGithubLink
        ) {
    public Link(String url) {
        this(url, null, null);
    }
    public Link(String url, Timestamp lastUpdate){
        this(url, lastUpdate, null);
    }
    public Link(String url, Boolean isGithubLink){
        this(url, null, isGithubLink);
    }
}
