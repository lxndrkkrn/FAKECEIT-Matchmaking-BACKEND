package org.example.fakeceit.DTOs.Response.Domain.User;

import org.example.fakeceit.Enum.UserState;

public record UserSetStateResponseDTO(
        Long id,
        UserState userState
) {
}
