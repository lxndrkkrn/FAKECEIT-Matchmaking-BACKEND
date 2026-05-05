package org.example.fakeceit.DTOs.Response.Domain.Invite;

import jakarta.validation.constraints.NotNull;

public record CreateInviteResponseDTO(
        @NotNull Long inviterId,
        @NotNull Long invitedId,
        @NotNull Long teamId
) {
}
