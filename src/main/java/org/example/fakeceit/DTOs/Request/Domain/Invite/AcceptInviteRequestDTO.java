package org.example.fakeceit.DTOs.Request.Domain.Invite;

import jakarta.validation.constraints.NotNull;

public record AcceptInviteRequestDTO(
        @NotNull Long acceptedId,
        @NotNull Long teamId
) {
}
