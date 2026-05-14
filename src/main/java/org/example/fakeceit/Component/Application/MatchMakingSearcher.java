package org.example.fakeceit.Component.Application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.*;
import org.example.fakeceit.Enum.SearchTicketStatePlayer;
import org.example.fakeceit.Enum.SearchTicketStateTeam;
import org.example.fakeceit.Repositories.SearchTicketRepository;
import org.example.fakeceit.Service.Application.Facade.GameLoopService;
import org.example.fakeceit.Service.Domain.LobbyService;
import org.example.fakeceit.Service.Domain.MapService;
import org.example.fakeceit.Service.Domain.TeamService;
import org.example.fakeceit.Service.Domain.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
@Validated

public class MatchMakingSearcher {

    private final TeamService teamService;
    private final LobbyService lobbyService;
    private final MapService mapService;
    private final SearchTicketRepository searchTicketRepository;
    private final UserService userService;
    private final GameLoopService gameLoopService;

    private final Integer maxEloDiff = 300;

    @Scheduled(fixedDelay = 5000)
    public void searchMatch() {
        List<SearchTicket> activeTickets = searchTicketRepository.findAllByStateTeam(SearchTicketStateTeam.ENABLE);

        for (SearchTicket ticketA : activeTickets) {
            Optional<SearchTicket> match = activeTickets.stream()
                    .filter(ticketB -> !ticketA.equals(ticketB))
                    .filter(ticketB -> ticketA.isCompatibleWith(ticketB, maxEloDiff))
                    .findFirst();

            if (match.isPresent()) {
                SearchTicket ticketB = match.get();

                ticketA.matchFound();
                ticketB.matchFound();

                searchTicketRepository.delete(ticketA);
                searchTicketRepository.delete(ticketB);

                teamService.transitionTeamToGame(ticketA.getId());
                teamService.transitionTeamToGame(ticketB.getId());

                lobbyService.createLobby(null, null, ticketA.getTeamId(), ticketB.getTeamId());

                log.info("Матч найден! Команда {} VS команды {}. Разница среднего ELO: {}.", ticketA.getTeamId(), match.get().getTeamId(), ticketA.getAverageElo() - match.get().getAverageElo());
                break;
            }
        }
    }

    @Scheduled(fixedDelay = 3000)
    public void searchPlayer() {
        List<SearchTicket> activePlayers = searchTicketRepository.findAllByStatePlayer(SearchTicketStatePlayer.SOLO);

        if (activePlayers.size() < 5) {
            return;
        }

        List<SearchTicket> group = activePlayers.subList(0, 5);

        List<Long> playerIds = group.stream()
                .flatMap(t -> t.getPlayerIds().stream())
                .toList();

        Team team = teamService.createTeam(group.getFirst().getId(), playerIds, null);

        group.forEach(SearchTicket::playersFound);
        gameLoopService.startSearchGame(team.getCaptain().getId());

        searchTicketRepository.deleteAll(group);

        log.info("Команда {} из рандомов создана и начала поиск противников", team.getId());
    }
}
