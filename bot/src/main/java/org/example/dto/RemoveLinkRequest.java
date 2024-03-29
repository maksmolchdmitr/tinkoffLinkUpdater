package org.example.dto;

import jakarta.validation.constraints.NotNull;

import java.net.URL;

public record RemoveLinkRequest(
        @NotNull URL link
) {
}
