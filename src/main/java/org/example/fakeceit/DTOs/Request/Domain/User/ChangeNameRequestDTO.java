package org.example.fakeceit.DTOs.Request.Domain.User;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeNameRequestDTO(
        Long id,

        @NotNull
        @Size(min = 3, max = 16, message = "Имя должно содержать от 3 до 16 символов")
        @Column(unique = true) String name
) {
}
