package org.example.fakeceit.DTOs.Request.Domain.Lobby;

import jakarta.validation.constraints.NotNull;
import org.example.fakeceit.Enum.LobbyStatus;

public record SetStatusLobbyRequestDTO(
        @NotNull Long id,
        @NotNull LobbyStatus lobbyStatus
) {
}
