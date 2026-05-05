package org.example.fakeceit.DTOs.Request.Domain.Statistic;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddStatisticRequestDTO(
        @NotNull Long userId,
        @NotNull Long teamId,
        @NotNull Integer newKills,
        @NotNull Integer newDeaths,
        @NotNull Boolean isWin
) {
}
