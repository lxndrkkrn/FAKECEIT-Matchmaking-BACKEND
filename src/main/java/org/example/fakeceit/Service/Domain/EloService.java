package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Elo.AddEloRequestDTO;
import org.example.fakeceit.DTOs.Request.Elo.SetEloRequestDTO;
import org.example.fakeceit.DTOs.Request.Elo.TakeEloRequestDTO;
import org.example.fakeceit.DTOs.Response.Elo.AddEloResponseDTO;
import org.example.fakeceit.DTOs.Response.Elo.SetEloResponseDTO;
import org.example.fakeceit.DTOs.Response.Elo.TakeEloResponseDTO;
import org.example.fakeceit.Entity.TransactionGame;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Enum.EloLevel;
import org.example.fakeceit.Exception.Client.InvalidValue;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.TransactionGameRepository;
import org.example.fakeceit.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated

public class EloService {

    private final UserRepository userRepository;
    private final TransactionGameRepository transactionGameRepository;

    @Transactional
    public SetEloResponseDTO setElo(@Valid SetEloRequestDTO setEloRequestDTO) {
        log.info("Попытка установить {} Elo пользователю с ID: {}", setEloRequestDTO.deltaElo(), setEloRequestDTO.id());

        TransactionGame transactionGame = new TransactionGame();
        User user = userRepository.findById(setEloRequestDTO.id()).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        int deltaElo = setEloRequestDTO.deltaElo();

        transactionGame.setAmountElo(setEloRequestDTO.deltaElo());
        transactionGame.setUser(user);

        updatePlayerElo(user, deltaElo);

        transactionGameRepository.save(transactionGame);
        userRepository.save(user);

        return new SetEloResponseDTO(
                transactionGame.getId(),
                user.getId(),
                setEloRequestDTO.deltaElo()
        );
    }

    @Transactional
    public AddEloResponseDTO addElo(@Valid AddEloRequestDTO addEloRequestDTO) {
        log.info("Попытка добавить {} Elo пользователю с ID: {}", addEloRequestDTO.deltaElo(), addEloRequestDTO.id());

        TransactionGame transactionGame = new TransactionGame();
        User user = userRepository.findById(addEloRequestDTO.id()).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        int deltaElo = addEloRequestDTO.deltaElo();

        transactionGame.setAmountElo(addEloRequestDTO.deltaElo());
        transactionGame.setUser(user);

        int totalElo = user.getElo() + deltaElo;

        updatePlayerElo(user, totalElo);

        transactionGameRepository.save(transactionGame);
        userRepository.save(user);

        return new AddEloResponseDTO(
                transactionGame.getId(),
                user.getId(),
                addEloRequestDTO.deltaElo()
        );
    }

    @Transactional
    public TakeEloResponseDTO takeElo(@Valid TakeEloRequestDTO takeEloRequestDTO) {
        log.info("Попытка снять {} Elo пользователю с ID: {}", takeEloRequestDTO.deltaElo(), takeEloRequestDTO.id());

        TransactionGame transactionGame = new TransactionGame();
        User user = userRepository.findById(takeEloRequestDTO.id()).orElseThrow(() -> new NotFound404("Пользователь не найден"));
        int deltaElo = takeEloRequestDTO.deltaElo();

        transactionGame.setAmountElo(takeEloRequestDTO.deltaElo() * -1);
        transactionGame.setUser(user);

        int totalElo = user.getElo() - deltaElo;

        updatePlayerElo(user, totalElo);

        transactionGameRepository.save(transactionGame);
        userRepository.save(user);

        return new TakeEloResponseDTO(
                transactionGame.getId(),
                user.getId(),
                takeEloRequestDTO.deltaElo()
        );
    }

    private void updatePlayerElo(User user, int newElo) {
        if (newElo < 100) {
            throw new InvalidValue("Elo не может быть меньше 100");
        }

        user.setElo(newElo);
        user.setLevel(EloLevel.getLevelById(newElo));
    }
}
