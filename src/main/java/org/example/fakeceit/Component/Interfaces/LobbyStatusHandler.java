package org.example.fakeceit.Component.Interfaces;

import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Enum.LobbyStatus;

public interface LobbyStatusHandler {

    LobbyStatus getTargetStatus();
    void handle(Lobby lobby);

}
