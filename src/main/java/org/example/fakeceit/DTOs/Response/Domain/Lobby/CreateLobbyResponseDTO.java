package org.example.fakeceit.DTOs.Response.Domain.Lobby;

import jakarta.validation.constraints.NotNull;

public record CreateLobbyResponseDTO(
        @NotNull Long id,
        @NotNull Long IP_id,
        @NotNull Long mapId,
        @NotNull Long teamA_id,
        @NotNull Long teamB_id
) {
}
