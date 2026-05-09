package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Transactional

public class EloService {

    private final UserRepository userRepository;
    private final TransactionGameRepository transactionGameRepository;

    public void setElo(Long id, Integer deltaElo) {
        log.info("Попытка установить {} Elo пользователю с ID: {}", deltaElo, id);

        TransactionGame transactionGame = new TransactionGame();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        transactionGame.setAmountElo(deltaElo);
        transactionGame.setUser(user);

        updatePlayerElo(user, deltaElo);

        transactionGameRepository.save(transactionGame);
        userRepository.save(user);
    }

    public void addElo(Long id, Integer deltaElo) {
        log.info("Попытка добавить {} Elo пользователю с ID: {}", deltaElo, id);

        TransactionGame transactionGame = new TransactionGame();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        transactionGame.setAmountElo(deltaElo);
        transactionGame.setUser(user);

        int totalElo = user.getElo() + deltaElo;

        updatePlayerElo(user, totalElo);

        transactionGameRepository.save(transactionGame);
        userRepository.save(user);
    }

    public void takeElo(Long id, Integer deltaElo) {
        log.info("Попытка снять {} Elo пользователю с ID: {}", deltaElo, id);

        TransactionGame transactionGame = new TransactionGame();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        transactionGame.setAmountElo(deltaElo * -1);
        transactionGame.setUser(user);

        int totalElo = user.getElo() - deltaElo;

        updatePlayerElo(user, totalElo);

        transactionGameRepository.save(transactionGame);
        userRepository.save(user);
    }

    private void updatePlayerElo(User user, int newElo) {
        if (newElo < 100) {
            throw new InvalidValue("Elo не может быть меньше 100");
        }

        user.setElo(newElo);
        user.setLevel(EloLevel.getLevelById(newElo));
    }
}
