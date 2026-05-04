package org.example.fakeceit.DTOs.Request.Team;

public record RemoveUserFromTeamRequestDTO(
        Long userId,
        Long teamId
) {
}
