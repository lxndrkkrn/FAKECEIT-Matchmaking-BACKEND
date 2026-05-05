package org.example.fakeceit.DTOs.Response.Domain.Elo;

public record TakeEloResponseDTO(
        Long transactionGameId,
        Long userId,
        Integer deltaElo
) {
}
