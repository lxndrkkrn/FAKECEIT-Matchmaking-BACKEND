package org.example.fakeceit.DTOs.Response.User;

import org.example.fakeceit.Enum.UserState;

public record UserSetStateResponseDTO(
        Long id,
        UserState userState
) {
}
