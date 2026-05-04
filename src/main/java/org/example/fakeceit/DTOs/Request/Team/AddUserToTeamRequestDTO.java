package org.example.fakeceit.DTOs.Request.Team;

public record AddUserToTeamRequestDTO(
        Long userId,
        Long teamId
) {
}
