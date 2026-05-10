package org.example.fakeceit.DTOs.Request.Invite;

public record InviteUserRequestDTO(
        Long userId,
        Long invitedId,
        Long teamId
) {
}
