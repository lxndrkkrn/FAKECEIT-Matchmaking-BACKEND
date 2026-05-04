package org.example.fakeceit.DTOs.Request.Team;

import jakarta.validation.constraints.NotNull;

public record SetStateForTeamRequestDTO(
        @NotNull Long id,
        @NotNull Boolean state
) {
}
