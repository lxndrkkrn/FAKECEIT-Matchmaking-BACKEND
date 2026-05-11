package org.example.fakeceit.DTOs.Request.Band;

public record KickUserRequestDTO(
        Long captainId,
        Long kickedId,
        Long teamId
) {
}
