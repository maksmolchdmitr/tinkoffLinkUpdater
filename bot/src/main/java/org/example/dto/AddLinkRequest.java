package org.example.dto;

import jakarta.validation.constraints.NotNull;

import java.net.URL;

public record AddLinkRequest(
        @NotNull URL link
) {
}
