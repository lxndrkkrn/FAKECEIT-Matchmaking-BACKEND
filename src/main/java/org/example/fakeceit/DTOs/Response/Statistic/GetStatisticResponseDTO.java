package org.example.fakeceit.DTOs.Response.Statistic;

public record GetStatisticResponseDTO(
        Long userId,
        Integer countMatches,
        Integer countWins,
        Integer countLoses,
        Integer countKills,
        Integer countDeaths,
        Double KD,
        Double WR
) {
}
