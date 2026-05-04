package org.example.fakeceit.DTOs.Response.Balance;

import org.example.fakeceit.Enum.TransactionBalanceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetTransactionBalanceResponseDTO(
        Long id,
        BigDecimal deltaBalance,
        TransactionBalanceType transactionBalanceType,
        Long userId,
        LocalDateTime localDateTime
) {
}
