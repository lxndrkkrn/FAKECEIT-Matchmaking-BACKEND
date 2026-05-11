package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "maps")
@Data

public class GameMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private Boolean isActive;

    @NotNull
    @URL(message = "Это не ссылка")
    private String iconImg;

    @NotNull
    @URL(message = "Это не ссылка")
    private String backgroundImg;

    @NotNull
    @URL(message = "Это не ссылка")
    private String bannerImg;
}
