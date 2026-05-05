package org.example.fakeceit.DTOs.Request.Domain.Lobby;

import jakarta.validation.constraints.NotNull;

public record CreateLobbyRequestDTO(
        @NotNull Long IP_id,
        @NotNull Long mapId,
        @NotNull Long teamA_id,
        @NotNull Long teamB_id
) {
}
