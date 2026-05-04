package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Balance.AddBalanceRequestDTO;
import org.example.fakeceit.DTOs.Request.Balance.GetTransactionBalanceRequestDTO;
import org.example.fakeceit.DTOs.Request.Balance.SetBalanceRequestDTO;
import org.example.fakeceit.DTOs.Request.Balance.TakeBalanceRequestDTO;
import org.example.fakeceit.DTOs.Response.Balance.AddBalanceResponseDTO;
import org.example.fakeceit.DTOs.Response.Balance.GetTransactionBalanceResponseDTO;
import org.example.fakeceit.DTOs.Response.Balance.SetBalanceResponseDTO;
import org.example.fakeceit.DTOs.Response.Balance.TakeBalanceResponseDTO;
import org.example.fakeceit.Entity.TransactionBalance;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Enum.TransactionBalanceType;
import org.example.fakeceit.Exception.Client.NegativeBalance;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.TransactionBalanceRepository;
import org.example.fakeceit.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated

public class BalanceService {

    private final UserRepository userRepository;
    private final TransactionBalanceRepository transactionBalanceRepository;

    @Transactional
    public SetBalanceResponseDTO setBalance(@Valid SetBalanceRequestDTO setBalanceRequestDTO) {
        log.info("Попытка установки баланса игроку");

        if (setBalanceRequestDTO.deltaBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalance("Нельзя установить отрицательный баланс");
        }

        User user = findById(setBalanceRequestDTO.id());

        user.setBalance(setBalanceRequestDTO.deltaBalance());

        createTransactionBalance(
                setBalanceRequestDTO.deltaBalance(),
                user,
                setBalanceRequestDTO.transactionBalanceType()
        );

        userRepository.save(user);

        return new SetBalanceResponseDTO(
                user.getId(),
                user.getBalance(),
                setBalanceRequestDTO.transactionBalanceType()
        );
    }

    @Transactional
    public AddBalanceResponseDTO addBalance(@Valid AddBalanceRequestDTO addBalanceRequestDTO) {
        log.info("Попытка добавления баланса игроку");

        if (addBalanceRequestDTO.deltaBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalance("Нельзя добавить отрицательный баланс");
        }

        User user = findById(addBalanceRequestDTO.id());

        user.setBalance(user.getBalance().add(addBalanceRequestDTO.deltaBalance()));

        createTransactionBalance(
                addBalanceRequestDTO.deltaBalance(),
                user,
                addBalanceRequestDTO.transactionBalanceType()
        );

        userRepository.save(user);

        return new AddBalanceResponseDTO(
                user.getId(),
                addBalanceRequestDTO.deltaBalance(),
                user.getBalance(),
                addBalanceRequestDTO.transactionBalanceType()
        );
    }

    @Transactional
    public TakeBalanceResponseDTO takeBalance(@Valid TakeBalanceRequestDTO takeBalanceRequestDTO) {
        log.info("Попытка убавления баланса игроку");

        User user = findById(takeBalanceRequestDTO.id());

        if (takeBalanceRequestDTO.deltaBalance().compareTo(user.getBalance()) > 0) {
            throw new NegativeBalance("Не дастаточно средств");
        }

        user.setBalance(user.getBalance().subtract(takeBalanceRequestDTO.deltaBalance()));

        createTransactionBalance(
                takeBalanceRequestDTO.deltaBalance().negate(),
                user,
                takeBalanceRequestDTO.transactionBalanceType()
        );

        userRepository.save(user);

        return new TakeBalanceResponseDTO(
                user.getId(),
                takeBalanceRequestDTO.deltaBalance(),
                user.getBalance(),
                takeBalanceRequestDTO.transactionBalanceType()
        );
    }

    @Transactional(readOnly = true)
    public GetTransactionBalanceResponseDTO getTransactionBalance(@Valid GetTransactionBalanceRequestDTO getTransactionBalanceRequestDTO) {
        TransactionBalance transactionBalance = transactionBalanceRepository.findById(getTransactionBalanceRequestDTO.id()).orElseThrow(() -> new NotFound404("Транзакция баланса не найдена"));

        return new GetTransactionBalanceResponseDTO(
                transactionBalance.getId(),
                transactionBalance.getDeltaBalance(),
                transactionBalance.getTransactionBalanceType(),
                transactionBalance.getUser().getId(),
                transactionBalance.getLocalDateTime()
        );
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

    @Transactional
    private void createTransactionBalance(BigDecimal deltaBalance, User user, TransactionBalanceType transactionBalanceType) {
        TransactionBalance transactionBalance = new TransactionBalance();

        transactionBalance.setDeltaBalance(deltaBalance);
        transactionBalance.setUser(user);
        transactionBalance.setTransactionBalanceType(transactionBalanceType);

        transactionBalanceRepository.save(transactionBalance);
    }

}
