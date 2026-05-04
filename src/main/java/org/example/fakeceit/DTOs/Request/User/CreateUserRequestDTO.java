package org.example.fakeceit.DTOs.Request;

import jakarta.validation.Valid;

public record CreateUserRequestDTO(
        @Valid String name,
        @Valid String password
) {
}
