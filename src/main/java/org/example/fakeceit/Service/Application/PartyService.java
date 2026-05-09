package org.example.fakeceit.Service.Application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Domain.Invite.AcceptInviteRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Invite.CreateInviteRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Invite.DeleteInviteRequestDTO;
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
@Validated
@RequiredArgsConstructor
@Transactional

public class PartyService {

    private final InviteService inviteService;
    private final UserService userService;
    private final TeamService teamService;

    public CreateInviteRequestDTO inviteUser(@Valid CreateInviteRequestDTO dto) {

        inviteService.createInvite(dto);
        log.info("Инвайт от {} для {} в команду {} успешно создан", dto.inviterId(), dto.invitedId(), dto.teamId());

        return dto;
    }

    public void cancelInvite(@Valid DeleteInviteRequestDTO dto) {

        inviteService.deleteInvite(dto);
        log.info("Инвайт с ID: {} отменён", dto.inviteId());
    }

    public void acceptInvite(@Valid AcceptInviteRequestDTO dto) {
        User acceptedUser = userService.findUserById(dto.acceptedId());
        Team team = teamService.findTeamById(dto.teamId());

        if (team.getPlayers().contains(acceptedUser)) {
            throw new Forbidden403("Вы уже состоите в этой команде");
        }

        AddUserToTeamRequestDTO addUserToTeamRequestDTO = new AddUserToTeamRequestDTO(acceptedUser.getId(), team.getId());

        teamService.addUserToTeam(addUserToTeamRequestDTO);
        log.info("Инвайт в команду с ID: {} принят игроком с ID: {}", dto.teamId(), dto.acceptedId());
    }
}
