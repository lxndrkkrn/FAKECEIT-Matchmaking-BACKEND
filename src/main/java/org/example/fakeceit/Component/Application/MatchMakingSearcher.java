package org.example.fakeceit.Component.Application;

import jakarta.persistence.Lob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.GameMap;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Entity.SearchTicket;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Enum.SearchTicketState;
import org.example.fakeceit.Repositories.SearchTicketRepository;
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
import java.util.Optional;

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

    private final Integer maxEloDiff = 300;

    @Scheduled(fixedDelay = 5000)
    public void searchMatch() {
        List<SearchTicket> activeTickets = searchTicketRepository.findAllByState(SearchTicketState.ENABLE);

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

                lobbyService.createLobby(null, null, ticketA.getTeamId(), ticketB.getTeamId());

                log.info("Матч найден! Команда {} VS команды {}. Разница среднего ELO: {}.", ticketA.getTeamId(), match.get().getTeamId(), ticketA.getAverageElo() - match.get().getAverageElo());
                break;
            }
        }
    }
}
