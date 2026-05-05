package org.example.fakeceit.DTOs.Response.Domain.Elo;

public record SetEloResponseDTO(
        Long transactionGameId,
        Long userId,
        Integer deltaElo
) {
}
