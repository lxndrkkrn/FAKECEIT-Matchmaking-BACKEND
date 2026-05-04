package org.example.fakeceit.DTOs.Response;

import java.time.LocalDateTime;

public record DeleteUserResponseDTO(
        Long id,
        LocalDateTime localDateTime
) {
}
