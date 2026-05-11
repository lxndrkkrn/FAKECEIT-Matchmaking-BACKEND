package org.example.fakeceit.Service.Application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Band.DisbandTeamRequestDTO;
import org.example.fakeceit.DTOs.Request.Band.KickUserRequestDTO;
import org.example.fakeceit.DTOs.Request.Band.LeaveTheTeamRequestDTO;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Exception.ClientHTTP.BadRequest400;
import org.example.fakeceit.Exception.ClientHTTP.Forbidden403;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
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

    public void kickUser(KickUserRequestDTO dto) {
        log.info("Попытка кикнуть игрока из команды");

        User captain = userService.findUserById(dto.captainId());
        User kicked = userService.findUserById(dto.kickedId());
        Team team = teamService.findTeamById(dto.teamId());

        boolean isSomeoneSearchingOrPlaying = team.getPlayers().stream()
                .anyMatch(p -> p.getIsSearchGame() || p.getIsInGame());

        if (!team.getCaptain().equals(captain)) {
            throw new Forbidden403("Вы не капитан, что бы кикать игрока из команды");
        }

        if (isSomeoneSearchingOrPlaying) {
            throw new BadRequest400("Нельзя кикнуть игрока: кто-то из членов команды уже ищет игру или находится в матче");
        }

        if (captain.equals(kicked)) {
            throw new BadRequest400("Вы не можете кикнуть сами себя");
        }

        teamService.removeUserFromTeam(kicked.getId(), team.getId());
    }

}
