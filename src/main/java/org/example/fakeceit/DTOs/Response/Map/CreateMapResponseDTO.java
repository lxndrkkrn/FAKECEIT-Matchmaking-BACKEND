package org.example.fakeceit.DTOs.Response.Map;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CreateMapResponseDTO(
        @NotNull Long id,
        @NotNull String name,
        @NotNull @URL(message = "Это не ссылка") String iconImg,
        @NotNull @URL(message = "Это не ссылка") String backgroundImg,
        @NotNull @URL(message = "Это не ссылка") String bannerImg
) {
}
