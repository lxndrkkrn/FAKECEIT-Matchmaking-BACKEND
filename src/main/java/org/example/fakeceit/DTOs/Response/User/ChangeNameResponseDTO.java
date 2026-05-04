package org.example.fakeceit.DTOs.Response.User;

import java.time.LocalDateTime;

public record ChangeNameResponseDTO(
        Long id,
        LocalDateTime localDateTime
) {
}
