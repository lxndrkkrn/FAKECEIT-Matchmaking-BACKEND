package org.example.fakeceit.Service.Application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.SSL;
import org.example.fakeceit.DTOs.Request.Band.DisbandTeamRequestDTO;
import org.example.fakeceit.DTOs.Request.Band.LeaveTheTeamRequestDTO;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Exception.ClientHTTP.Forbidden403;
import org.example.fakeceit.Service.Domain.InviteService;
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

public class PartyBandService {

    private final InviteService inviteService;
    private final UserService userService;
    private final TeamService teamService;

    public void leaveTheTeam(LeaveTheTeamRequestDTO dto) {
        log.info("Попытка выйти из команды");

        User leaver = userService.findUserById(dto.leaverId());
        Team team = teamService.findTeamById(dto.teamId());

        if (!team.getPlayers().contains(leaver)) {
            throw new Forbidden403("Вы не состоите в этой команде");
        }

        if (leaver.getIsSearchGame() || leaver.getIsInGame()) {
            throw new Forbidden403("Вы не можете покинуть команду, пока ищите игру или находитесь в ней");
        }

        if (team.getCaptain().equals(leaver)) {
            teamService.deleteTeam(team.getId());
            log.info("Удалён капитан. Команда распущена (удалена).");
            return;
        }

        teamService.removeUserFromTeam(leaver.getId(), team.getId());
    }

    public void disbandTeam(DisbandTeamRequestDTO dto) {
        log.info("Попытка роспуска команды");

        User user = userService.findUserById(dto.captainId());
        Team team = teamService.findTeamById(dto.teamId());

        if (!team.getPlayers().contains(user)) {
            throw new Forbidden403("Вы не можете распустить команду, не состоя в ней");
        }

        if (!team.getCaptain().equals(user)) {
            throw new Forbidden403("Вы не можете распустить команду не являясь её капитаном");
        }

        teamService.deleteTeam(team.getId());
    }

}
