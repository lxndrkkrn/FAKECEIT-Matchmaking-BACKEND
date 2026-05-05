package org.example.fakeceit.DTOs.Request.Domain.User;

import jakarta.validation.Valid;

public record CreateUserRequestDTO(
        @Valid String name,
        @Valid String password
) {
}
