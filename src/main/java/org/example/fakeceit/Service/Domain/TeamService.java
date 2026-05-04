package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Team.*;
import org.example.fakeceit.DTOs.Response.Team.*;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Exception.Client.InvalidValue;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.TeamRepository;
import org.example.fakeceit.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated

public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public CreateTeamResponseDTO createTeam(@Valid CreateTeamRequestDTO createTeamRequestDTO) {
        log.info("Попытка создания команды");

        if (createTeamRequestDTO.players().size() > 5) {
            throw new InvalidValue("Некоректный размер команды (должен быть от 1 до 5 включительно)");
        }

        Team team = new Team();

        team.setCaptain(userRepository.findById(createTeamRequestDTO.captainId()).orElseThrow(() -> new NotFound404("Пользователь не найден")));
        team.setPlayers(userRepository.findAllById(createTeamRequestDTO.players()));
        team.setName(createTeamRequestDTO.name());

        teamRepository.save(team);

        return new CreateTeamResponseDTO(
                team.getId(),
                createTeamRequestDTO.players(),
                team.getName(),
                team.getCaptain().getId()
        );
    }

    @Transactional
    public DeleteTeamResponseDTO deleteTeam(@Valid DeleteTeamRequestDTO deleteTeamRequestDTO) {
        log.info("Попытка удаления команды");

        Team team = teamRepository.findById(deleteTeamRequestDTO.id()).orElseThrow(() -> new NotFound404("Команда не найдена"));

        teamRepository.delete(team);

        return new DeleteTeamResponseDTO(
                team.getId()
        );
    }

    @Transactional
    public AddUserToTeamResponseDTO addUserToTeam(@Valid AddUserToTeamRequestDTO addUserToTeamRequestDTO) {
        log.info("Попытка приглашения игрока с ID: {} в команду с ID: {}", addUserToTeamRequestDTO.userId(), addUserToTeamRequestDTO.teamId());

        User user = userRepository.findById(addUserToTeamRequestDTO.userId()).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        Team team = teamRepository.findById(addUserToTeamRequestDTO.teamId()).orElseThrow(() -> new NotFound404("Команда не найдена"));

        if (team.getPlayers().size() >= 5) {
            throw new InvalidValue("В команде уже максимум игроков (5)");
        }

        if (team.getPlayers().contains(user)) {
            throw new InvalidValue("Игрок уже состоит в этой команде");
        }

        if (team.getName() == null && team.getCaptain() != null) {
            team.setName(team.getCaptain().getName() + "_team");
        }

        team.getPlayers().add(user);

        teamRepository.save(team);

        return new AddUserToTeamResponseDTO(
                team.getId(),
                user.getId()
        );
    }

    @Transactional
    public RemoveUserFromTeamResponseDTO removeUserFromTeam(RemoveUserFromTeamRequestDTO removeUserFromTeamRequestDTO) {
        log.info("Попытка удалить игрока с ID: {} из команды с ID: {}", removeUserFromTeamRequestDTO.userId(), removeUserFromTeamRequestDTO.teamId());

        Team team = teamRepository.findById(removeUserFromTeamRequestDTO.teamId()).orElseThrow(() -> new NotFound404("Команда не найдена"));

        if (team.getPlayers().isEmpty()) {
            throw new InvalidValue("Команда пуста");
        }

        User user = userRepository.findById(removeUserFromTeamRequestDTO.userId()).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (team.getCaptain().equals(user)) {
            log.info("Удалён капитан. Команда распущена (удалена).");

            deleteTeam(new DeleteTeamRequestDTO(
                    removeUserFromTeamRequestDTO.teamId()
            ));

            return new RemoveUserFromTeamResponseDTO(
                    removeUserFromTeamRequestDTO.teamId()
            );
        }

        team.getPlayers().remove(user);
        user.getTeamList().remove(team);

        teamRepository.save(team);

        return new RemoveUserFromTeamResponseDTO(
                removeUserFromTeamRequestDTO.teamId()
        );
    }

    @Transactional
    public SetUserAsCaptainResponseDTO setUserAsCaptain(SetUserAsCaptainRequestDTO setUserAsCaptainRequestDTO) {
        log.info("Попытка установить пользователя с ID: {} капитаном команды с ID: {}", setUserAsCaptainRequestDTO.userId(), setUserAsCaptainRequestDTO.teamId());

        Team team = teamRepository.findById(setUserAsCaptainRequestDTO.teamId()).orElseThrow(() -> new NotFound404("Команда не найдена"));

        if (team.getPlayers().isEmpty()) {
            throw new InvalidValue("Команда пуста");
        }

        User user = userRepository.findById(setUserAsCaptainRequestDTO.userId()).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (!team.getPlayers().contains(user)) {
            throw new NotFound404("Пользователь в этой команде не найден. Возможно, он существует в другом месте.");
        }

        team.setCaptain(user);

        teamRepository.save(team);

        return new SetUserAsCaptainResponseDTO(
                team.getCaptain().getId(),
                team.getId()
        );
    }

    @Transactional
    public SetNameForTeamResponseDTO setNameForTeam(SetNameForTeamRequestDTO setNameForTeamRequestDTO) {
        log.info("Попытка смены названия для команды с ID: {}", setNameForTeamRequestDTO.id());

        Team team = teamRepository.findById(setNameForTeamRequestDTO.id()).orElseThrow(() -> new NotFound404("Команда не найдена"));

        team.setName(setNameForTeamRequestDTO.name());

        teamRepository.save(team);

        return new SetNameForTeamResponseDTO(
                team.getId(),
                team.getName()
        );
    }

    @Transactional
    public SetStateForTeamResponseDTO setStateForTeam(SetStateForTeamRequestDTO setStateForTeamRequestDTO) {
        log.info("Попытка смены состояния для команды с ID: {}", setStateForTeamRequestDTO.id());

        Team team = teamRepository.findById(setStateForTeamRequestDTO.id()).orElseThrow(() -> new NotFound404("Команда не найдена"));

        team.setIsWinner(setStateForTeamRequestDTO.state());

        teamRepository.save(team);

        return new SetStateForTeamResponseDTO(
                team.getId(),
                team.getIsWinner()
        );
    }

}
