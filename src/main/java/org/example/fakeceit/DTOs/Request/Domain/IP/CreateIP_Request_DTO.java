package org.example.fakeceit.DTOs.Request.Domain.IP;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.fakeceit.Enum.ServerRegion;
import org.example.fakeceit.Enum.ServerStatus;

public record CreateIP_Request_DTO(
        @NotNull @Column(unique = true) String ip,
        @NotNull Integer port,
        @NotNull ServerStatus status,
        @NotNull ServerRegion region,
        @NotNull @Size(min = 6, message = "RCON пароль должен содержать минимум 6 символов") String rcon
) {

}
