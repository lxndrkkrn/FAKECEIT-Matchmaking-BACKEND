package org.example.fakeceit.DTOs.Response.Elo;

public record SetEloResponseDTO(
        Long transactionGameId,
        Long userId,
        Integer deltaElo
) {
}
