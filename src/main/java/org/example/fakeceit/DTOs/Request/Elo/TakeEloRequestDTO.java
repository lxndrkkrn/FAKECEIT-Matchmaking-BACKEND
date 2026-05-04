package org.example.fakeceit.DTOs.Request.Elo;

import jakarta.validation.constraints.Min;

public record TakeEloRequestDTO(
        Long id,
        @Min(1) Integer deltaElo
) {
}
