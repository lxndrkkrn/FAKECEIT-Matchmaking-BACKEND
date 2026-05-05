package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Domain.Statistic.AddStatisticRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Statistic.GetStatisticRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Statistic.ResetStatisticRequestDTO;
import org.example.fakeceit.DTOs.Response.Domain.Statistic.AddStatisticResponseDTO;
import org.example.fakeceit.DTOs.Response.Domain.Statistic.GetStatisticResponseDTO;
import org.example.fakeceit.DTOs.Response.Domain.Statistic.ResetStatisticResponseDTO;
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

    public AddStatisticResponseDTO addStatistic(AddStatisticRequestDTO addStatisticRequestDTO) {
        log.info("Попытка обновления статистики игроку");

        Team team = teamRepository.findById(addStatisticRequestDTO.teamId()).orElseThrow(() -> new NotFound404("Команда не найдена"));
        User user = userRepository.findById(addStatisticRequestDTO.userId()).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        if (!team.getPlayers().contains(user)) {
            throw new NotFound404("Пользователь в данной команде не найден");
        }

        Statistic statistic = user.getStatistic();

        statistic.updateStats(addStatisticRequestDTO.newKills(), addStatisticRequestDTO.newDeaths(), addStatisticRequestDTO.isWin());

        return new AddStatisticResponseDTO(
                user.getId()
        );
    }

    public ResetStatisticResponseDTO resetStatistic(ResetStatisticRequestDTO resetStatisticRequestDTO) {
        log.info("Попытка сброса статистики");

        User user = userRepository.findById(resetStatisticRequestDTO.userId()).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        Statistic statistic = user.getStatistic();

        statistic.resetStats();

        return new ResetStatisticResponseDTO(
                user.getId()
        );
    }

    @Transactional(readOnly = true)
    public GetStatisticResponseDTO getStatistic(GetStatisticRequestDTO getStatisticRequestDTO) {
        log.info("Попытка просмотра статистики");

        User user = userRepository.findById(getStatisticRequestDTO.id()).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        Statistic statistic = user.getStatistic();

        return new GetStatisticResponseDTO(
                user.getId(),
                statistic.getCountMatches(),
                statistic.getCountWins(),
                statistic.getCountLoses(),
                statistic.getCountKills(),
                statistic.getCountDeaths(),
                statistic.getKD(),
                statistic.getWR()
        );
    }

}
