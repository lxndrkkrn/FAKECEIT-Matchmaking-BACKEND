package org.example.fakeceit.DTOs.Request.Elo;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record SetEloRequestDTO(
        Long id,
        @Min(1) Integer deltaElo
) {
}
