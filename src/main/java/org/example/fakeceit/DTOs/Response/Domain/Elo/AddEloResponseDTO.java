package org.example.fakeceit.DTOs.Response.Domain.Elo;

public record AddEloResponseDTO(
        Long transactionGameId,
        Long userId,
        Integer deltaElo
) {
}
