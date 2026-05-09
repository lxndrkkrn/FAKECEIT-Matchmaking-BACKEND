package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Transactional

public class BalanceService {

    private final UserRepository userRepository;
    private final TransactionBalanceRepository transactionBalanceRepository;

    public void setBalance(Long id, BigDecimal deltaBalance, TransactionBalanceType transactionBalanceType) {
        log.info("Попытка установки баланса игроку");

        if (deltaBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalance("Нельзя установить отрицательный баланс");
        }

        User user = findById(id);

        user.setBalance(deltaBalance);

        createTransactionBalance(
                deltaBalance,
                user,
                transactionBalanceType
        );

        userRepository.save(user);
    }

    public void addBalance(Long id, BigDecimal deltaBalance, TransactionBalanceType transactionBalanceType) {
        log.info("Попытка добавления баланса игроку");

        if (deltaBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalance("Нельзя добавить отрицательный баланс");
        }

        User user = findById(id);

        user.setBalance(user.getBalance().add(deltaBalance));

        createTransactionBalance(
                deltaBalance,
                user,
                transactionBalanceType
        );

        userRepository.save(user);
    }

    public void takeBalance(Long id, BigDecimal deltaBalance, TransactionBalanceType transactionBalanceType) {
        log.info("Попытка убавления баланса игроку");

        User user = findById(id);

        if (deltaBalance.compareTo(user.getBalance()) > 0) {
            throw new NegativeBalance("Не дастаточно средств");
        }

        user.setBalance(user.getBalance().subtract(deltaBalance));

        createTransactionBalance(
                deltaBalance.negate(),
                user,
                transactionBalanceType
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TransactionBalance getTransactionBalance(Long id) {
        return transactionBalanceRepository.findById(id).orElseThrow(() -> new NotFound404("Транзакция баланса не найдена"));
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

    private void createTransactionBalance(BigDecimal deltaBalance, User user, TransactionBalanceType transactionBalanceType) {
        TransactionBalance transactionBalance = new TransactionBalance();

        transactionBalance.setDeltaBalance(deltaBalance);
        transactionBalance.setUser(user);
        transactionBalance.setTransactionBalanceType(transactionBalanceType);

        transactionBalanceRepository.save(transactionBalance);
    }

}
