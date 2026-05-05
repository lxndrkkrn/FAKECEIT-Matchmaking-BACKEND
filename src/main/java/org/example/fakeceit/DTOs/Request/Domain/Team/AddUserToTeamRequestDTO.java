package org.example.fakeceit.DTOs.Request.Domain.Team;

public record AddUserToTeamRequestDTO(
        Long userId,
        Long teamId
) {
}
