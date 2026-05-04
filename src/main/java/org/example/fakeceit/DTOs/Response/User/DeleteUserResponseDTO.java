package org.example.fakeceit.DTOs.Response.User;

import java.time.LocalDateTime;

public record DeleteUserResponseDTO(
        Long id,
        LocalDateTime localDateTime
) {
}
