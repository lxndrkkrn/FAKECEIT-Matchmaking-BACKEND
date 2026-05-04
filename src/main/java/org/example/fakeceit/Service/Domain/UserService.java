package org.example.fakeceit.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.User.*;
import org.example.fakeceit.DTOs.Response.User.*;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Errors.Client.IncorrectName;
import org.example.fakeceit.Errors.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public CreateUserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        log.info("Попытка создания пользователя");

        if (userRepository.existsByName(createUserRequestDTO.name())) {
            throw new IncorrectName("Это имя уже занято");
        }

        User user = new User();

        user.setName(createUserRequestDTO.name());
        user.setPassword(createUserRequestDTO.password());

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

    @Transactional
    public DeleteUserResponseDTO deleteUser(DeleteUserRequestDTO deleteUserRequestDTO) {
        log.info("Попытка удаления пользователя с ID: {}", deleteUserRequestDTO.id());

        User user = findUserById(deleteUserRequestDTO.id());

        DeleteUserResponseDTO deleteUserResponseDTO = new DeleteUserResponseDTO(
                deleteUserRequestDTO.id(),
                LocalDateTime.now()
        );

        userRepository.delete(user);
        log.info("Пользователь с ID: {} удалён", deleteUserRequestDTO.id());

        return deleteUserResponseDTO;
    }

    @Transactional
    public ChangeNameResponseDTO changeName(ChangeNameRequestDTO changeNameRequestDTO) {
        log.info("Попытка изменения имени пользователю с ID: {}", changeNameRequestDTO.id());

        User user = findUserById(changeNameRequestDTO.id());

        user.setName(changeNameRequestDTO.name());
        userRepository.save(user);

        ChangeNameResponseDTO changeNameResponseDTO = new ChangeNameResponseDTO(
                changeNameRequestDTO.id(),
                LocalDateTime.now()
        );

        userRepository.delete(user);
        log.info("Имя пользователю с ID: {} изменено", changeNameRequestDTO.id());

        return changeNameResponseDTO;
    }

    @Transactional
    public ChangePasswordResponseDTO changePass(ChangePasswordRequestDTO changePasswordRequestDTO) {
        log.info("Попытка изменения пароля пользователю с ID: {}", changePasswordRequestDTO.id());

        User user = findUserById(changePasswordRequestDTO.id());

        user.setPassword(changePasswordRequestDTO.password());
        userRepository.save(user);

        ChangePasswordResponseDTO changePasswordResponseDTO = new ChangePasswordResponseDTO(
                changePasswordRequestDTO.id(),
                user.getName(),
                LocalDateTime.now()
        );

        userRepository.delete(user);
        log.info("Пароль пользователю с ID: {} изменен", changePasswordRequestDTO.id());

        return changePasswordResponseDTO;
    }

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

    public UserResponseDTO findUserByName(FindUserByNameRequestDTO findUserByNameRequestDTO) {
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

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

}
