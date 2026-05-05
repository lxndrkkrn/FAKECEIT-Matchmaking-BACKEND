package org.example.fakeceit.DTOs.Request.Domain.Team;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateTeamRequestDTO(
        @NotNull Long captainId,
        @NotNull @NotEmpty @Size(min = 1, max = 5, message = "Команда может содержать только от 1 до 5 игроков") List<Long> players,
        String name

) {
}
