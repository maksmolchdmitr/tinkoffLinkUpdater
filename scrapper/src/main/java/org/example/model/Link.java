package org.example.model;

import java.sql.Timestamp;

public record Link(String url, Timestamp lastUpdate) {
    public Link(String url) {
        this(url, null);
    }
}
