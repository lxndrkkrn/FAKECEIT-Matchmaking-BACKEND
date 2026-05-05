package org.example.fakeceit.DTOs.Request.Domain.Team;

import jakarta.validation.constraints.NotNull;

public record RemoveUserFromTeamRequestDTO(
        @NotNull Long userId,
        @NotNull Long teamId
) {
}
