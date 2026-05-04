package org.example.fakeceit.DTOs.Request.Team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SetNameForTeamRequestDTO(
        @NotNull Long id,
        @NotNull @NotBlank String name
) {
}
