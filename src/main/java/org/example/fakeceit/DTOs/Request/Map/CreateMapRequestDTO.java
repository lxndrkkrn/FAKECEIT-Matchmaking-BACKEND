package org.example.fakeceit.DTOs.Request.Map;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CreateMapRequestDTO(
        @NotNull @Column(unique = true) String name,
        @NotNull @URL(message = "Это не ссылка") String iconImg,
        @NotNull @URL(message = "Это не ссылка") String backgroundImg,
        @NotNull @URL(message = "Это не ссылка") String bannerImg
) {
}
