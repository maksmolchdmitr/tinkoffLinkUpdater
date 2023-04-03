package dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateRequest(@NotNull Long id,
                            @NotNull String url,
                            String description,
                            @NotNull
                            @Size(min = 1)
                            List<Long> tgChatIds) {
}
