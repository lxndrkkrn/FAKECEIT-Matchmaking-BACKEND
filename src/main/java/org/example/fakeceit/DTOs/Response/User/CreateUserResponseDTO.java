package org.example.fakeceit.DTOs.Response.User;

import org.example.fakeceit.Enum.EloLevel;

import java.math.BigDecimal;

public record CreateUserResponseDTO(
        Long id,
        String name,
        BigDecimal balance,
        Integer elo,
        EloLevel level,
        Boolean sub
) {
}
