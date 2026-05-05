package org.example.fakeceit.Component.Status;

import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Component.Interfaces.LobbyStatusHandler;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Enum.LobbyStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j

public class InProgressStatusHandler implements LobbyStatusHandler {

    @Override
    public LobbyStatus getTargetStatus() {
        return LobbyStatus.IN_PROGRESS;
    }

    @Override
    public void handle(Lobby lobby) {
        log.info("Лобби с ID {} начало матч", lobby.getId());
        lobby.setInProgressAt(LocalDateTime.now());
    }
}
