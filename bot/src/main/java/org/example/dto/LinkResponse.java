package org.example.dto;

import jakarta.validation.constraints.NotNull;

public record LinkResponse(
        @NotNull Long id,
        @NotNull String url
) {
}
