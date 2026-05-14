package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fakeceit.Enum.SearchTicketStatePlayer;
import org.example.fakeceit.Enum.SearchTicketStateTeam;
import org.example.fakeceit.Exception.ClientHTTP.BadRequest400;

import java.util.List;

@Entity
@Table(name = "search_tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SearchTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SearchTicketStateTeam stateTeam;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SearchTicketStatePlayer statePlayer;

    @NotNull
    @Size(min = 1, max = 5, message = "Начать поиск матча можно только в группе от 0 до 5 игроков")
    private Integer groupSize;

    @NotNull
    private Double averageElo;

    private Long teamId;

    @ElementCollection
    @CollectionTable(name = "ticket_player_id", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "player_id")
    private List<Long> playerIds;

    @Version
    private Long version;

    public static SearchTicket createForTeam(Team team) {

        if (team.getPlayers().size() != 5) {
            throw new BadRequest400("Команда должна состоять ровно из 5 человек");
        }

        double averageElo = team.getPlayers().stream()
                .mapToDouble(User::getElo)
                .average()
                .orElse(0.0);

        return SearchTicket.builder()
                .stateTeam(SearchTicketStateTeam.ENABLE)
                .statePlayer(SearchTicketStatePlayer.TEAM)
                .groupSize(team.getPlayers().size())
                .averageElo(averageElo)
                .playerIds(team.getPlayers().stream().map(User::getId).toList())
                .teamId(team.getId())
                .build();
    }

    public static SearchTicket createForPlayer(User user, Team team) {

        return SearchTicket.builder()
                .stateTeam(SearchTicketStateTeam.DISABLE)
                .statePlayer(SearchTicketStatePlayer.SOLO)
                .groupSize(team.getPlayers().size())
                .playerIds(List.of(user.getId()))
                .averageElo(user.getElo().doubleValue())
                .build();
    }

    public void matchFound() {
        if (this.stateTeam != SearchTicketStateTeam.ENABLE) {
            throw new BadRequest400("Нельзя найти матч, пока он не встал в очередь");
        }
        this.stateTeam = SearchTicketStateTeam.DISABLE;
    }

    public void playersFound() {
        if (this.statePlayer == SearchTicketStatePlayer.ASSEMBLED) {
            throw new BadRequest400("Нельзя найти игроков, когда вы уже сгруппированны");
        }
        this.statePlayer = SearchTicketStatePlayer.ASSEMBLED;
    }

    public void cancel() {
        this.stateTeam = SearchTicketStateTeam.DISABLE;
        this.statePlayer = SearchTicketStatePlayer.ASSEMBLED;
    }

    public boolean isCompatibleWith(SearchTicket other, int maxEloDiff) {
        if (this.teamId != null && other.getTeamId() != null && this.teamId.equals(other.getTeamId())) {
            return false;
        }


        if (this.groupSize + other.groupSize != 10) {
            return false;
        }

        if (this.teamId.equals(other.getTeamId())) {
            return false;
        }

        return Math.abs(this.averageElo - other.getAverageElo()) <= maxEloDiff;
    }

}
