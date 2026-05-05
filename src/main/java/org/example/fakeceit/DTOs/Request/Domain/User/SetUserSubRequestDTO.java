package org.example.fakeceit.DTOs.Request.Domain.User;

import org.example.fakeceit.Enum.UserState;

public record SetUserSubRequestDTO(
        Long id,
        Boolean sub
) {
}
