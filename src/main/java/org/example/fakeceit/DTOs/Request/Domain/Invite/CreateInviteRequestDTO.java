package org.example.fakeceit.DTOs.Request.Domain.Invite;

import jakarta.validation.constraints.NotNull;

public record CreateInviteRequestDTO(
        @NotNull Long inviterId,
        @NotNull Long invitedId,
        @NotNull Long teamId
) {
}
