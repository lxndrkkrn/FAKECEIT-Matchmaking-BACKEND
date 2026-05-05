package org.example.fakeceit.DTOs.Response.Domain.User;

import java.time.LocalDateTime;

public record ChangeNameResponseDTO(
        Long id,
        LocalDateTime localDateTime
) {
}
