package org.example.fakeceit.DTOs.Response.User;

import java.time.LocalDateTime;

public record ChangePasswordResponseDTO(
        Long id,
        String name,
        LocalDateTime localDateTime
) {
}
