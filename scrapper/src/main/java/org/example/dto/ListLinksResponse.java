package org.example.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ListLinksResponse(
        @NotNull List<LinkResponse> links,
        @NotNull Integer size
) {
}
