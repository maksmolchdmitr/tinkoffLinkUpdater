package org.example.model;

import java.sql.Timestamp;

public record Link(long id, String url, Timestamp lastUpdate) {}
