package org.example.fakeceit.DTOs.Request.Balance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.fakeceit.Enum.TransactionBalanceType;

import java.math.BigDecimal;

public record AddBalanceRequestDTO(
        @NotNull Long id,
        @Positive(message = "Дельта-баланс должен быть положительным") BigDecimal deltaBalance,
        @NotNull TransactionBalanceType transactionBalanceType
) {
}
