package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Domain.Invite.CreateInviteRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Invite.DeleteInviteRequestDTO;
import org.example.fakeceit.Entity.Invite;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Exception.ClientHTTP.BadRequest400;
import org.example.fakeceit.Exception.ClientHTTP.Forbidden403;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Exception.ServerHTTP.ServiceUnavailable503;
import org.example.fakeceit.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class InviteService {

    private final InviteRepository inviteRepository;
    private final LobbyRepository lobbyRepository;
    private final MapRepository mapRepository;
    private final TeamRepository teamRepository;
    private final IPRepository ipRepository;
    private final UserRepository userRepository;

    public void createInvite(Long inviterId, Long invitedId, Long teamId) {
        log.info("Попытка создания приглашения");

        Invite invite = new Invite();

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new NotFound404("Команда не найдена"));
        User inviter = userRepository.findById(inviterId).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        User invited = userRepository.findById(invitedId).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (inviter.getTeamList().contains(team)) {
            throw new BadRequest400("Инициатор приглашения не состоит в команде");
        }
        if (!invited.getTeamList().isEmpty()) {
            throw new BadRequest400("Приглашённый уже состоит в команде");
        }
        if (team.getPlayers().size() >= 5) {
            throw new BadRequest400("Команда полностью заполнена");
        }

        invite.setTeam(team);
        invite.setInviter(inviter);
        invite.setInvited(invited);

        inviteRepository.save(invite);
    }

    public void deleteInvite(Long inviteId, Long deinviterId, Long invitedId) {
        log.info("Попытка удаления приглашения");

        Invite invite = inviteRepository.findById(inviteId).orElseThrow(() -> new NotFound404("Приглашение не найдено"));
        User deinviter = userRepository.findById(deinviterId).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        User invited = userRepository.findById(invitedId).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (!invite.getInviter().equals(deinviter) && !invite.getInvited().equals(invited)) {
            throw new Forbidden403("Вы не можете удалять чужое приглашение");
        }

        inviteRepository.delete(invite);
    }

}
