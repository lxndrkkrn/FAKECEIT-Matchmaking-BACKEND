package org.example.fakeceit.DTOs.Request.Team;

public record SetUserAsCaptainRequestDTO(
        Long userId,
        Long teamId
) {
}
