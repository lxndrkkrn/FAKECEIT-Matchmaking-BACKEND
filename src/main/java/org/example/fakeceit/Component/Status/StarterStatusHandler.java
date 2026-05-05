package org.example.fakeceit.Component.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Component.Interfaces.LobbyStatusHandler;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Enum.LobbyStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class StarterStatusHandler implements LobbyStatusHandler {

    @Override
    public LobbyStatus getTargetStatus() {
        return LobbyStatus.STARTED;
    }

    @Override
    public void handle(Lobby lobby) {
        log.info("Лобби с ID {} запущено", lobby.getId());
        lobby.setStartedAt(LocalDateTime.now());
    }
}
