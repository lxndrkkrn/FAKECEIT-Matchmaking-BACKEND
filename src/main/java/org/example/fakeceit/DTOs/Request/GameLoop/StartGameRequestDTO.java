package org.example.fakeceit.DTOs.Request.GameLoop;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record StartGameRequestDTO(
        @Size(min = 0, max = 4, message = "Обычных игроков в команде должно быть от 0 до 4 человек") List<Long> playersId,
        @NotNull @Max(value = 1, message = "В команде может быть 1 капитан") Long captainId
) {
}
