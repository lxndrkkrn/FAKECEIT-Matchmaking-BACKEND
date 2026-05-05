package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Domain.User.*;
import org.example.fakeceit.DTOs.Response.Domain.User.*;
import org.example.fakeceit.Entity.Statistic;
import org.example.fakeceit.Entity.User;
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

    public CreateUserResponseDTO createUser(@Valid CreateUserRequestDTO createUserRequestDTO) {
        log.info("Попытка создания пользователя");

        if (userRepository.existsByName(createUserRequestDTO.name())) {
            throw new IncorrectName("Это имя уже занято");
        }

        Statistic statistic = new Statistic();

        User user = new User();

        user.setName(createUserRequestDTO.name());
        user.setPassword(createUserRequestDTO.password());
        user.setStatistic(statistic);

        userRepository.save(user);
        log.info("Пользователь создан, ID: {}", user.getId());

        CreateUserResponseDTO createUserResponseDTO = new CreateUserResponseDTO(
                user.getId(),
                createUserRequestDTO.name(),
                user.getBalance(),
                user.getElo(),
                user.getLevel(),
                user.getSub()
        );

        return createUserResponseDTO;
    }

    public DeleteUserResponseDTO deleteUser(@Valid DeleteUserRequestDTO deleteUserRequestDTO) {
        log.info("Попытка удаления пользователя с ID: {}", deleteUserRequestDTO.id());

        User user = findUserById(deleteUserRequestDTO.id());
        Statistic statistic = user.getStatistic();

        DeleteUserResponseDTO deleteUserResponseDTO = new DeleteUserResponseDTO(
                deleteUserRequestDTO.id(),
                LocalDateTime.now()
        );

        statisticRepository.delete(statistic);
        userRepository.delete(user);

        log.info("Пользователь с ID: {} удалён", deleteUserRequestDTO.id());

        return deleteUserResponseDTO;
    }

    public ChangeNameResponseDTO changeName(@Valid ChangeNameRequestDTO changeNameRequestDTO) {
        log.info("Попытка изменения имени пользователю с ID: {}", changeNameRequestDTO.id());

        User user = findUserById(changeNameRequestDTO.id());

        user.setName(changeNameRequestDTO.name());
        userRepository.save(user);

        ChangeNameResponseDTO changeNameResponseDTO = new ChangeNameResponseDTO(
                changeNameRequestDTO.id(),
                LocalDateTime.now()
        );

        log.info("Имя пользователю с ID: {} изменено", changeNameRequestDTO.id());

        return changeNameResponseDTO;
    }

    public ChangePasswordResponseDTO changePass(@Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
        log.info("Попытка изменения пароля пользователю с ID: {}", changePasswordRequestDTO.id());

        User user = findUserById(changePasswordRequestDTO.id());

        user.setPassword(changePasswordRequestDTO.password());
        userRepository.save(user);

        ChangePasswordResponseDTO changePasswordResponseDTO = new ChangePasswordResponseDTO(
                changePasswordRequestDTO.id(),
                user.getName(),
                LocalDateTime.now()
        );

        log.info("Пароль пользователю с ID: {} изменен", changePasswordRequestDTO.id());

        return changePasswordResponseDTO;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findUserById(FindUserByIdRequestDTO findUserByIdRequestDTO) {
        User user = userRepository.findById(findUserByIdRequestDTO.id()).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getBalance(),
                user.getElo(),
                user.getLevel(),
                user.getSub()
        );
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findUserByName(@Valid FindUserByNameRequestDTO findUserByNameRequestDTO) {
        User user = userRepository.findUserByName(findUserByNameRequestDTO.name()).orElseThrow(() -> new NotFound404("Пользователь не найден"));

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getBalance(),
                user.getElo(),
                user.getLevel(),
                user.getSub()
        );
    }

    public UserSetStateResponseDTO setUserState(UserSetStateRequestDTO setStateRequestDTO) {
        log.info("Попытка установить статус пользователю");
        User user = findUserById(setStateRequestDTO.id());

        user.setUserState(setStateRequestDTO.userState());

        return new UserSetStateResponseDTO(
                user.getId(),
                user.getUserState()
        );
    }


    public SetUserSubResponseDTO setUserSub(SetUserSubRequestDTO setUserSubRequestDTO) {
        log.info("Попытка установить подписку пользователю");
        User user = findUserById(setUserSubRequestDTO.id());

        user.setSub(setUserSubRequestDTO.sub());

        return new SetUserSubResponseDTO(
                user.getId(),
                user.getSub()
        );
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

}
