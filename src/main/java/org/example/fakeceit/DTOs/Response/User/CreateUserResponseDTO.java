package org.example.fakeceit.DTOs.Response;

import java.math.BigDecimal;

public record CreateUserResponseDTO(
        Long id,
        String name,
        BigDecimal balance,
        Integer elo,
        Integer level,
        Boolean sub
) {
}
