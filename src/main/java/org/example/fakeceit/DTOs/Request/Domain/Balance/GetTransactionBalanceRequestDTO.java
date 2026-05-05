package org.example.fakeceit.DTOs.Request.Domain.Balance;

import jakarta.validation.constraints.NotNull;

public record GetTransactionBalanceRequestDTO(
        @NotNull Long id
) {
}
