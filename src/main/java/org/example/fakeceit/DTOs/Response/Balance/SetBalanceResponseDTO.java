package org.example.fakeceit.DTOs.Response.Balance;

import org.example.fakeceit.Enum.TransactionBalanceType;

import java.math.BigDecimal;

public record SetBalanceResponseDTO(
        Long userId,
        BigDecimal newBalance,
        TransactionBalanceType transactionBalanceType
) {
}
