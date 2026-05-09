package org.example.fakeceit.Service.Domain;

import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Component.Interfaces.LobbyStatusHandler;
import org.example.fakeceit.Entity.IP;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Entity.GameMap;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Enum.LobbyStatus;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.IPRepository;
import org.example.fakeceit.Repositories.LobbyRepository;
import org.example.fakeceit.Repositories.MapRepository;
import org.example.fakeceit.Repositories.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
//@RequiredArgsConstructor
@Validated
@Transactional

public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final MapRepository mapRepository;
    private final TeamRepository teamRepository;
    private final IPRepository ipRepository;

    private final Map<LobbyStatus, LobbyStatusHandler> statusHandler;

    public LobbyService(LobbyRepository lobbyRepository,
                        MapRepository mapRepository,
                        TeamRepository teamRepository,
                        IPRepository ipRepository,
                        List<LobbyStatusHandler> handlers) {

        this.lobbyRepository = lobbyRepository;
        this.mapRepository = mapRepository;
        this.teamRepository = teamRepository;
        this.ipRepository = ipRepository;

        this.statusHandler = handlers.stream()
                .collect(java.util.stream.Collectors.toMap(LobbyStatusHandler::getTargetStatus, h -> h));
    }


    public Lobby createLobby(Long IP_id, Long mapId, Long teamA_id, Long teamB_id) {
        log.info("Попытка создания лобби");

        Lobby lobby = new Lobby();

        IP ip = ipRepository.findById(IP_id).orElseThrow(() -> new NotFound404("IP адреса с таким ID не существует"));
        GameMap gameMap = mapRepository.findById(mapId).orElseThrow(() -> new NotFound404("Такой карты не существует"));
        Team teamA = teamRepository.findById(teamA_id).orElseThrow(() -> new NotFound404("Такой команды не существует"));
        Team teamB = teamRepository.findById(teamB_id).orElseThrow(() -> new NotFound404("Такой команды не существует"));

        lobby.setIp(ip);
        lobby.setGameMap(gameMap);
        lobby.setTeamA(teamA);
        lobby.setTeamB(teamB);

        lobbyRepository.save(lobby);

        return lobby;
    }

    public void deleteLobby(Long id) {
        log.info("Попытка удаления лобби");

        Lobby lobby = lobbyRepository.findById(id).orElseThrow(() -> new NotFound404("Лобби не найдено"));

        lobbyRepository.delete(lobby);
    }

    public void setStatusLobby(Long id, LobbyStatus lobbyStatus) {
        log.info("Попытка смены статуса для лобби");

        Lobby lobby = lobbyRepository.findById(id).orElseThrow(() -> new NotFound404("Лобби не найдено"));

        lobby.setStatus(lobbyStatus);

        LobbyStatusHandler handler = statusHandler.get(lobbyStatus);

        if (handler != null) {
            handler.handle(lobby);
        }
    }

}
