package org.example.fakeceit.DTOs.Response.Elo;

public record AddEloResponseDTO(
        Long transactionGameId,
        Long userId,
        Integer deltaElo
) {
}
