package org.example.fakeceit.DTOs.Request.User;

import org.example.fakeceit.Enum.UserState;

public record UserSetStateRequestDTO(
        Long id,
        UserState userState
) {
}
