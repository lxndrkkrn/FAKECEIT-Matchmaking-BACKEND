package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "invites")
@Data@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "invited_id")
    private User invited;

    @NotNull
    private Boolean state = true;

    @NotNull
    private LocalDateTime inviteDate = LocalDateTime.now();

    @NotNull
    private LocalDateTime finishInviteDate = LocalDateTime.now().plusMinutes(5);
}
