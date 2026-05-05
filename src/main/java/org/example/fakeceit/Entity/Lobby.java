package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.fakeceit.Enum.LobbyStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "lobbies")
@Data

public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ip_id")
    private IP ip;

    @ManyToOne
    @JoinColumn(name = "map_id")
    private GameMap gameMap;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "teamA_id", referencedColumnName = "id")
    private Team teamA;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "teamB_id", referencedColumnName = "id")
    private Team teamB;

    private Integer scoreA = 0;

    private Integer scoreB = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LobbyStatus status = LobbyStatus.CREATED;

    @NotNull
    private LocalDateTime createAt = LocalDateTime.now();
    private LocalDateTime startedAt; //
    private LocalDateTime pickedServerAt; //
    private LocalDateTime pickedMapAt; //
    private LocalDateTime warmupAt; //
    private LocalDateTime inProgressAt; //
    private LocalDateTime finishedAt; //

    private LocalDateTime waitingAt;
    private LocalDateTime cancelledAt;

}
