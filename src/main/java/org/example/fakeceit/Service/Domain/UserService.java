package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.Statistic;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Enum.UserState;
import org.example.fakeceit.Exception.Client.IncorrectName;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.StatisticRepository;
import org.example.fakeceit.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class UserService {

    private final UserRepository userRepository;
    private final StatisticRepository statisticRepository;

    public User createUser(String name, String password) {
        log.info("Попытка создания пользователя");

        if (userRepository.existsByName(name)) {
            throw new IncorrectName("Это имя уже занято");
        }

        User user = new User();
        Statistic statistic = new Statistic();

        user.setName(name);
        user.setPassword(password);
        user.setStatistic(statistic);

        userRepository.save(user);
        log.info("Пользователь создан, ID: {}", user.getId());

        return user;
    }

    public void deleteUser(Long id) {
        log.info("Попытка удаления пользователя с ID: {}", id);

        User user = findUserById(id);
        Statistic statistic = user.getStatistic();

        statisticRepository.delete(statistic);
        userRepository.delete(user);

        log.info("Пользователь с ID: {} удалён", id);
    }

    public void changeName(Long id, String name) {
        log.info("Попытка изменения имени пользователю с ID: {}", id);

        User user = findUserById(id);

        user.setName(name);
        userRepository.save(user);

        log.info("Имя пользователю с ID: {} изменено", id);
    }

    public void changePass(Long id, String password) {
        log.info("Попытка изменения пароля пользователю с ID: {}", id);

        User user = findUserById(id);

        user.setPassword(password);
        userRepository.save(user);

        log.info("Пароль пользователю с ID: {} изменен", id);
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

    @Transactional(readOnly = true)
    public User findUserByName(String name) {
        return userRepository.findUserByName(name).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

    public void setUserState(Long id, UserState userState) {
        log.info("Попытка установить статус пользователю");
        User user = findUserById(id);

        user.setUserState(userState);
    }

    public void setUserSub(Long id, Boolean sub) {
        log.info("Попытка установить подписку пользователю");
        User user = findUserById(id);

        user.setSub(sub);
    }

    public void setUserSearchState(Long id, Boolean state) {
        log.info("Попытка установить состояние поиска игры пользователю");
        User user = findUserById(id);

        user.setIsSearchGame(state);
    }

    public void setUserGameState(Long id, Boolean state) {
        log.info("Попытка установить состояние нахождения в игре пользователю");
        User user = findUserById(id);

        user.setIsInGame(state);
    }
}
