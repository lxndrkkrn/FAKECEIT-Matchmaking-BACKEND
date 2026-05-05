package org.example.fakeceit.DTOs.Response.Domain.IP;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import org.example.fakeceit.Enum.ServerRegion;
import org.example.fakeceit.Enum.ServerStatus;

public record CreateIP_Response_DTO(
        Long id,
        String ip,
        Integer port,
        ServerStatus status,
        ServerRegion region
) {
}
