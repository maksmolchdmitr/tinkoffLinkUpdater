package org.example.dto;

import jakarta.validation.constraints.NotNull;

public record AddLinkRequest(
        @NotNull String link
) {
}
