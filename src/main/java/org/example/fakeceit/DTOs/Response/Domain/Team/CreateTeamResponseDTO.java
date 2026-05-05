package org.example.fakeceit.DTOs.Response.Domain.Team;

import java.util.List;

public record CreateTeamResponseDTO(
        Long id,
        List<Long> playersId,
        String name,
        Long captainId
) {
}
