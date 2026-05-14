package org.example.fakeceit.Service.Application.Facade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.GameLoop.StartGameRequestDTO;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Entity.SearchTicket;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Exception.ClientHTTP.Forbidden403;
import org.example.fakeceit.Repositories.SearchTicketRepository;
import org.example.fakeceit.Service.Domain.LobbyService;
import org.example.fakeceit.Service.Domain.SearchTicketService;
import org.example.fakeceit.Service.Domain.TeamService;
import org.example.fakeceit.Service.Domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class GameLoopService {

    private final UserService userService;
    private final TeamService teamService;
    private final LobbyService lobbyService;
    private final SearchTicketService searchTicketService;

    public void startSearchTeam(Long userId) {
        User user = userService.findUserById(userId);
        Team team = teamService.findOneTeamFromUser(user.getId());

        if (team.getPlayers().size() == 5) {
            startSearchGame(user.getId());

        } else {

            SearchTicket searchTicket = searchTicketService.createSearchTicketPlayer(user, team);

        }
        //NOT CREATED
        //Coming soon...
    }

    public void startSearchGame(Long userId) {
        User user = userService.findUserById(userId);
        Team team = teamService.findOneTeamFromUser(user.getId());

        if (!team.getCaptain().equals(user)) {
            throw new Forbidden403("Только капитан может начать игру");
        }

        Boolean playersIsInSearch = team.getPlayers().stream().anyMatch(User::getIsSearchGame);
        Boolean playersIsInGame = team.getPlayers().stream().anyMatch(User::getIsInGame);

        if (playersIsInSearch || playersIsInGame) {
            throw new Forbidden403("Нельзя начать игру, если её игроки её уже ищут или играют");
        }

        if (team.getIsSearchGame() || team.getIsInGame()) {
            throw new Forbidden403("Нельзя начать игру, если команда её уже ищет или играет");
        }

        teamService.setTeamSearchState(team.getId(), true);
        team.getPlayers().forEach(plr -> plr.setIsSearchGame(true));

        SearchTicket searchTicket = searchTicketService.createSearchTicketTeam(team);

        log.info("Команда {} (капитан: {}) начала поиск матча", team.getId(), team.getCaptain().getId());
    }

}
