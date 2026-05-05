package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Component.Interfaces.LobbyStatusHandler;
import org.example.fakeceit.DTOs.Request.Domain.Lobby.CreateLobbyRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Lobby.DeleteLobbyRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Lobby.SetStatusLobbyRequestDTO;
import org.example.fakeceit.DTOs.Response.Domain.Lobby.CreateLobbyResponseDTO;
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


    public CreateLobbyResponseDTO createLobby(@Valid CreateLobbyRequestDTO createLobbyRequestDTO) {
        log.info("Попытка создания лобби");

        Lobby lobby = new Lobby();

        IP ip = ipRepository.findById(createLobbyRequestDTO.IP_id()).orElseThrow(() -> new NotFound404("IP адреса с таким ID не существует"));
        GameMap gameMap = mapRepository.findById(createLobbyRequestDTO.mapId()).orElseThrow(() -> new NotFound404("Такой карты не существует"));
        Team teamA = teamRepository.findById(createLobbyRequestDTO.teamA_id()).orElseThrow(() -> new NotFound404("Такой команды не существует"));
        Team teamB = teamRepository.findById(createLobbyRequestDTO.teamB_id()).orElseThrow(() -> new NotFound404("Такой команды не существует"));

        lobby.setIp(ip);
        lobby.setGameMap(gameMap);
        lobby.setTeamA(teamA);
        lobby.setTeamB(teamB);

        lobbyRepository.save(lobby);

        return new CreateLobbyResponseDTO(
                lobby.getId(),
                lobby.getIp().getId(),
                lobby.getGameMap().getId(),
                lobby.getTeamA().getId(),
                lobby.getTeamB().getId()
        );
    }

    public void deleteLobby(@Valid DeleteLobbyRequestDTO deleteLobbyRequestDTO) {
        log.info("Попытка удаления лобби");

        Lobby lobby = lobbyRepository.findById(deleteLobbyRequestDTO.id()).orElseThrow(() -> new NotFound404("Лобби не найдено"));

        lobbyRepository.delete(lobby);
    }

    public void setStatusLobby(@Valid SetStatusLobbyRequestDTO setStatusLobbyRequestDTO) {
        log.info("Попытка смены статуса для лобби");

        Lobby lobby = lobbyRepository.findById(setStatusLobbyRequestDTO.id()).orElseThrow(() -> new NotFound404("Лобби не найдено"));
        LobbyStatus status = setStatusLobbyRequestDTO.lobbyStatus();

        lobby.setStatus(status);

        LobbyStatusHandler handler = statusHandler.get(status);

        if (handler != null) {
            handler.handle(lobby);
        }
    }

}
