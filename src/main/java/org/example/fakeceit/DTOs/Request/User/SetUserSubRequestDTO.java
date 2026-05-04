package org.example.fakeceit.DTOs.Request.User;

import org.example.fakeceit.Enum.UserState;

public record SetUserSubRequestDTO(
        Long id,
        Boolean sub
) {
}
