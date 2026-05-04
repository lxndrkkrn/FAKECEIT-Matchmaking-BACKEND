package org.example.fakeceit.DTOs.Request.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
        Long id,

        @NotNull
        @Size(min = 2, message = "Пароль должен содержать минимум 6 символов")
        String password
) {
}
