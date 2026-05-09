package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.Statistic;
import org.example.fakeceit.Entity.Team;
import org.example.fakeceit.Entity.User;
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
@Transactional

public class StatisticService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public Statistic addStatistic(Long userId, Long teamId, Integer newKills, Integer newDeaths, Boolean isWin) {
        log.info("Попытка обновления статистики игроку");

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new NotFound404("Команда не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (!team.getPlayers().contains(user)) {
            throw new NotFound404("Пользователь в данной команде не найден");
        }

        Statistic statistic = user.getStatistic();

        statistic.updateStats(newKills, newDeaths, isWin);

        return statistic;
    }

    public void resetStatistic(Long id) {
        log.info("Попытка сброса статистики");

        User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        Statistic statistic = user.getStatistic();

        statistic.resetStats();
    }

    @Transactional(readOnly = true)
    public Statistic getStatistic(Long id) {
        log.info("Попытка просмотра статистики");

        User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        return user.getStatistic();
    }

}
