package org.example.fakeceit.DTOs.Request.Domain.Team;

public record SetUserAsCaptainRequestDTO(
        Long userId,
        Long teamId
) {
}
