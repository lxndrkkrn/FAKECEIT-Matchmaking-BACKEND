package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.fakeceit.Enum.LobbyStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "current_turn_team_id")
    private Team currentTurnTeam;

    @ManyToMany
    @JoinTable(name = "lobby_banned_maps")
    private List<GameMap> bannedMaps = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "selected_map_id")
    private GameMap selectedMap;

    @ManyToMany
    @JoinTable(name = "lobby_banned_ips")
    private List<IP> bannedIps = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "selected_ip_id")
    private IP selectedIp;

}
