package org.example.fakeceit.Component.Application;

import jakarta.persistence.Lob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.GameMap;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Repositories.TeamRepository;
import org.example.fakeceit.Service.Domain.LobbyService;
import org.example.fakeceit.Service.Domain.MapService;
import org.example.fakeceit.Service.Domain.TeamService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
@Validated

public class MatchMakingSearcher {

    private final TeamService teamService;
    private final LobbyService lobbyService;
    private final MapService mapService;

    @Scheduled(fixedDelay = 5000)
    public void searchTeams() {
        List<Team> teams = teamService.findAllIsSearchGameTrue();

        if (teams.size() < 2) {
            return;
        }

        Team teamA = teams.getFirst();

        Team teamB = teams.stream()
                .filter(t -> !t.getId().equals(teamA.getId()))
                .findFirst()
                .orElse(null);

        if (teamB != null) {
            teamA.setIsSearchGame(false);
            teamB.setIsSearchGame(false);

            Lobby lobby = lobbyService.createLobby(null, null, teamA.getId(), teamB.getId());

            log.info("Матч найден: команда {} против команды {}. Лобби: {}", teamA.getId(), teamB.getId(), lobby.getId());
        }
    }
}
