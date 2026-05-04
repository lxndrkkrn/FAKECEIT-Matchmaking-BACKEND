package org.example.fakeceit.DTOs.Response.Elo;

public record TakeEloResponseDTO(
        Long transactionGameId,
        Long userId,
        Integer deltaElo
) {
}
