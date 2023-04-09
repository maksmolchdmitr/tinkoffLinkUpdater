package org.example.model;

import java.sql.Timestamp;

public record Link(long id, String url, Timestamp lastUpdate) {
    public Link(String url) {
        this(0, url, null);
    }
}
