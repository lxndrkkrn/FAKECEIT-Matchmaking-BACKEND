package org.example.fakeceit.Service.Application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Invite.AcceptInviteRequestDTO;
import org.example.fakeceit.DTOs.Request.Invite.CancelInviteRequestDTO;
import org.example.fakeceit.DTOs.Request.Invite.InviteUserRequestDTO;
import org.example.fakeceit.DTOs.Response.InviteUserResponseDTO;
import org.example.fakeceit.Entity.Invite;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Exception.ClientHTTP.BadRequest400;
import org.example.fakeceit.Exception.ClientHTTP.Forbidden403;
import org.example.fakeceit.Service.Domain.InviteService;
import org.example.fakeceit.Service.Domain.TeamService;
import org.example.fakeceit.Service.Domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
@Transactional

public class PartyInviteService {

    private final InviteService inviteService;
    private final UserService userService;
    private final TeamService teamService;

    public InviteUserResponseDTO inviteUser(InviteUserRequestDTO dto) {
        log.info("Попытка пригласить игрока {} игроком {} в команду {}", dto.invitedId(), dto.userId(), dto.teamId());
        User user = userService.findUserById(dto.userId());
        User invited = userService.findUserById(dto.invitedId());

        Team team = dto.teamId() == null ? null : teamService.findOptionalTeamById(dto.teamId()).orElse(null);

        if (user.equals(invited)) {
            throw new BadRequest400("Нельзя пригласить самого себя");
        }

        if (team == null)  {
            log.info("У игрока {} нет команды. Создаем автоматическую парти.", user.getId());
            List<Long> players = new ArrayList<>();
            players.add(user.getId());

            Team newTeam = teamService.createTeam(user.getId(), players, user.getName() + "_Team");

            Invite invite = inviteService.createInvite(user.getId(), invited.getId(), newTeam.getId());

            return new InviteUserResponseDTO(
                    invite.getId()
            );
        }

        Invite invite = inviteService.createInvite(user.getId(), invited.getId(), team.getId());

        return new InviteUserResponseDTO(
                invite.getId()
        );
    }

    public void acceptInvite(AcceptInviteRequestDTO dto) {
        Invite invite = inviteService.findInviteById(dto.inviteId());

        User currentUser = userService.findUserById(dto.userId());

        Team team = invite.getTeam();
        User user = invite.getInvited();

        log.info("Попытка игроком {} принять инвайт {}", user.getId(), currentUser.getId());

        if (!currentUser.equals(user)) {
            throw new Forbidden403("Вы не можете принять чужое приглашение");
        }

        if (user.getTeamList() != null && user.getTeamList().size() == 1) {
            Team oldTeam = user.getTeamList().getFirst();

            if (oldTeam.getCaptain().equals(user) && oldTeam.getPlayers().size() == 1) {
                log.info("Удаление команды {} (Последний игрок переходит в другую команду)", oldTeam.getId());
                teamService.deleteTeam(oldTeam.getId());
            }
        }

        teamService.addUserToTeam(user.getId(), team.getId());
        inviteService.setStateFromInvite(invite.getId() ,false);
    }

    public void cancelInvite(CancelInviteRequestDTO dto) {
        Invite invite = inviteService.findAnyInviteById(dto.inviteId());
        User currentUser = userService.findUserById(dto.userId());

        boolean isInviter = invite.getInviter().equals(currentUser);
        boolean isInvited = invite.getInvited().equals(currentUser);

        if (!isInviter && !isInvited) {
            throw new Forbidden403("Вы не можете отменять чужой инвайт");
        }

        log.info("Инвайт {} успешно отменен пользователем {}", invite.getId(), currentUser.getId());

        inviteService.setStateFromInvite(invite.getId(), false);
    }
}
