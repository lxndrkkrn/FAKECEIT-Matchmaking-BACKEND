package org.example.fakeceit.Service.Domain;

import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Component.Interfaces.LobbyStatusHandler;
import org.example.fakeceit.Entity.*;
import org.example.fakeceit.Enum.LobbyStatus;
import org.example.fakeceit.Exception.ClientHTTP.BadRequest400;
import org.example.fakeceit.Exception.ClientHTTP.Forbidden403;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.*;
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
    private final UserRepository userRepository;

    private final Map<LobbyStatus, LobbyStatusHandler> statusHandler;

    public LobbyService(LobbyRepository lobbyRepository,
                        MapRepository mapRepository,
                        TeamRepository teamRepository,
                        IPRepository ipRepository, UserRepository userRepository,
                        List<LobbyStatusHandler> handlers) {

        this.lobbyRepository = lobbyRepository;
        this.mapRepository = mapRepository;
        this.teamRepository = teamRepository;
        this.ipRepository = ipRepository;
        this.userRepository = userRepository;

        this.statusHandler = handlers.stream()
                .collect(java.util.stream.Collectors.toMap(LobbyStatusHandler::getTargetStatus, h -> h));
    }


    public Lobby createLobby(Long IP_id, Long mapId, Long teamA_id, Long teamB_id) {
        log.info("Попытка создания лобби");

        Lobby lobby = new Lobby();

        //IP ip = ipRepository.findById(IP_id).orElseThrow(() -> new NotFound404("IP адреса с таким ID не существует"));
        //GameMap gameMap = mapRepository.findById(mapId).orElseThrow(() -> new NotFound404("Такой карты не существует"));
        Team teamA = teamRepository.findById(teamA_id).orElseThrow(() -> new NotFound404("Такой команды не существует"));
        Team teamB = teamRepository.findById(teamB_id).orElseThrow(() -> new NotFound404("Такой команды не существует"));

        //lobby.setIp(ip);
        //lobby.setGameMap(gameMap);
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

    public void banIp(Long lobbyId, Long captainId, Long ipId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(() -> new NotFound404("Лобби не найдено"));
        User captain = userRepository.findById(captainId).orElseThrow(() -> new NotFound404("Игрок не найден"));
        IP ip = ipRepository.findById(ipId).orElseThrow(() -> new NotFound404("IP не найден"));

        if (lobby.getStatus() != LobbyStatus.PICKING_SERVER) {
            throw new BadRequest400("Время выбора IP вышло и не настало");
        }

        if (!lobby.getCurrentTurnTeam().getCaptain().equals(captain)) {
            throw new Forbidden403("Сейчас банит команда-противник");
        }

        if (lobby.getBannedIps().contains(ip)) {
            throw new BadRequest400("Этот IP уже забанен");
        }

        lobby.getBannedIps().add(ip);

        long totalIpsCount = ipRepository.count();
        long bannedIpsCount = lobby.getBannedIps().size();

        if (totalIpsCount - bannedIpsCount == 1) {
            IP selectedIp = ipRepository.findRemainingIp(lobby.getId());

            lobby.setIp(selectedIp);
            lobby.setStatus(LobbyStatus.PICKING_MAP);

            log.info("IP выбран автоматически: {}. Переходим к выбору карты.", selectedIp.getIp());
        } else {
            Team nextTeam = lobby.getCurrentTurnTeam().equals(lobby.getTeamA()) ? lobby.getTeamB() : lobby.getTeamA();
            lobby.setCurrentTurnTeam(nextTeam);
        }
    }

    public void banMap(Long lobbyId, Long captainId, Long mapId) {

        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(() -> new NotFound404("Лобби не найдено"));
        User captain = userRepository.findById(captainId).orElseThrow(() -> new NotFound404("Игрок не найден"));
        GameMap map = mapRepository.findById(mapId).orElseThrow(() -> new NotFound404("Карта не найдена"));

        if (lobby.getStatus() != LobbyStatus.PICKING_MAP) {
            throw new BadRequest400("Бан карт не начался или закончился");
        }

        if (!lobby.getCurrentTurnTeam().getCaptain().equals(captain)) {
            throw new Forbidden403("Только капитан может банить карты");
        }

        if (lobby.getBannedMaps().contains(map)) {
            throw new BadRequest400("Эта карта уже забанена");
        }

        lobby.getBannedMaps().add(map);

        long totalMapsCount = mapRepository.count();
        long bannedMapsCount = lobby.getBannedMaps().size();

        if (totalMapsCount - bannedMapsCount == 1) {
            GameMap selectedMap = mapRepository.findRemainingMap(lobby.getId());

            lobby.setGameMap(selectedMap);
            lobby.setStatus(LobbyStatus.WARMUP);

            log.info("Карта выбрана автоматически: {}. Переходим к ожиданию игроков (разминка).", selectedMap.getName());
        } else {
            Team nextTeam = lobby.getCurrentTurnTeam().equals(lobby.getTeamA()) ? lobby.getTeamB() : lobby.getTeamA();
            lobby.setCurrentTurnTeam(nextTeam);
        }
    }

    private void setStateFromAllEntity(Lobby lobby, Team team, User user) {

    }

}
