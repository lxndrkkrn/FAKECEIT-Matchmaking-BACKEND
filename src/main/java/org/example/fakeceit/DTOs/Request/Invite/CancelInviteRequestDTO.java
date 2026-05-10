package org.example.fakeceit.DTOs.Request.Invite;

public record CancelInviteRequestDTO(
        Long userId,
        Long inviteId
) {
}
