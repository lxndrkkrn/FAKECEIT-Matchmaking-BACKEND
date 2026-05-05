package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.fakeceit.Enum.ServerRegion;
import org.example.fakeceit.Enum.ServerStatus;

@Entity
@Table(name = "IPs")
@Data

public class IP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String ip;

    @NotNull
    @Column(unique = true)
    private Integer port;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServerStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServerRegion region;

    @NotNull
    @Size(min = 6, message = "RCON пароль должен содержать минимум 6 символов")
    private String rcon;

}
