package org.example.fakeceit.Service.Application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.MatchMacking.SearchMatchRequestDTO;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Service.Domain.IPService;
import org.example.fakeceit.Service.Domain.LobbyService;
import org.example.fakeceit.Service.Domain.TeamService;
import org.example.fakeceit.Service.Domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@Validated

public class MatchMakingService {

    private final UserService userService;
    private final TeamService teamService;
    private final LobbyService lobbyService;
    private final IPService ipService;

    public void searchMatch(SearchMatchRequestDTO dto) {
        Team teamA = teamService.findTeamById(dto.teamId());
        Optional<Team> teamB = teamService.findLookingForTeam(teamA.getId());
    }

}
