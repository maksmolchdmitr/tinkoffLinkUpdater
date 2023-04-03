package org.example.dto;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(
        @NotNull String link
) {
}
