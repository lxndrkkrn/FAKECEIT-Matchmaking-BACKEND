package org.example.fakeceit.DTOs.Response.Balance;

import org.example.fakeceit.Enum.TransactionBalanceType;

import java.math.BigDecimal;

public record AddBalanceResponseDTO(
        Long id,
        BigDecimal deltaBalance,
        BigDecimal newBalance,
        TransactionBalanceType transactionBalanceType
) {
}
