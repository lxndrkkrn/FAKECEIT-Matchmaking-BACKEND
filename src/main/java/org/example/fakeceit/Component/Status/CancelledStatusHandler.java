package org.example.fakeceit.Component.Status;

import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Component.Interfaces.LobbyStatusHandler;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Enum.LobbyStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j

public class CancelledStatusHandler implements LobbyStatusHandler {

    @Override
    public LobbyStatus getTargetStatus() {
        return LobbyStatus.CANCELLED;
    }

    @Override
    public void handle(Lobby lobby) {
        log.info("Лобби с ID {} отменило игру", lobby.getId());
        lobby.setCancelledAt(LocalDateTime.now());
    }
}
