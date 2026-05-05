package org.example.fakeceit.DTOs.Request.Domain.Invite;

import jakarta.validation.constraints.NotNull;

public record DeleteInviteRequestDTO(
        @NotNull Long inviteId,
        @NotNull Long deinviterId,
        @NotNull Long invitedId
) {
}
