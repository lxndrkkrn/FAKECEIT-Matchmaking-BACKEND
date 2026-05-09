package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Domain.Team.*;
import org.example.fakeceit.DTOs.Response.Domain.Team.*;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Exception.Client.InvalidValue;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.TeamRepository;
import org.example.fakeceit.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public Team createTeam(Long captainId, List<Long> players, String name) {
        log.info("Попытка создания команды");

        if (players.size() > 5) {
            throw new InvalidValue("Некоректный размер команды (должен быть от 1 до 5 включительно)");
        }

        Team team = new Team();

        team.setCaptain(userRepository.findById(captainId).orElseThrow(() -> new NotFound404("Пользователь не найден")));
        team.setPlayers(userRepository.findAllById(players));
        team.setName(name);

        teamRepository.save(team);

        return team;
    }

    public void deleteTeam(Long id) {
        log.info("Попытка удаления команды");

        Team team = teamRepository.findById(id).orElseThrow(() -> new NotFound404("Команда не найдена"));

        teamRepository.delete(team);
    }

    public Team addUserToTeam(Long userId, Long teamId) {
        log.info("Попытка приглашения игрока с ID: {} в команду с ID: {}", userId, teamId);

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new NotFound404("Команда не найдена"));

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

        return team;
    }

    public Team removeUserFromTeam(Long userId, Long teamId) {
        log.info("Попытка удалить игрока с ID: {} из команды с ID: {}", userId, teamId);

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new NotFound404("Команда не найдена"));

        if (team.getPlayers().isEmpty()) {
            throw new InvalidValue("Команда пуста");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (team.getCaptain().equals(user)) {
            log.info("Удалён капитан. Команда распущена (удалена).");
            return team;
        }

        team.getPlayers().remove(user);
        user.getTeamList().remove(team);

        teamRepository.save(team);

        return team;
    }

    public Team setUserAsCaptain(Long userId, Long teamId) {
        log.info("Попытка установить пользователя с ID: {} капитаном команды с ID: {}", userId, teamId);

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new NotFound404("Команда не найдена"));

        if (team.getPlayers().isEmpty()) {
            throw new InvalidValue("Команда пуста");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (!team.getPlayers().contains(user)) {
            throw new NotFound404("Пользователь в этой команде не найден. Возможно, он существует в другом месте.");
        }

        team.setCaptain(user);

        teamRepository.save(team);

        return team;
    }

    public void setNameForTeam(Long id, String name) {
        log.info("Попытка смены названия для команды с ID: {}", id);

        Team team = teamRepository.findById(id).orElseThrow(() -> new NotFound404("Команда не найдена"));

        team.setName(name);

        teamRepository.save(team);
    }

    public void setStateForTeam(Long id, Boolean state) {
        log.info("Попытка смены состояния для команды с ID: {}", id);

        Team team = teamRepository.findById(id).orElseThrow(() -> new NotFound404("Команда не найдена"));

        team.setIsWinner(state);

        teamRepository.save(team);
    }

    public Team findTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new NotFound404("Команда не найдена"));
    }

}
