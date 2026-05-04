package org.example.fakeceit.DTOs.Request.Elo;

import jakarta.validation.constraints.Min;

public record AddEloRequestDTO(
        Long id,
        @Min(1) Integer deltaElo
) {
}
