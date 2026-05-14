package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fakeceit.Enum.SearchTicketState;
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
    private SearchTicketState state;

    @NotNull
    @Size(min = 0, max = 5, message = "Начать поиск матча можно только в группе от 0 до 5 игроков")
    private Integer groupSize;

    @NotNull
    private Double averageElo;

    private Long teamId;

    @ElementCollection
    @CollectionTable(name = "ticket_player_id", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "player_id")
    private List<Long> playerId;

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
                .state(SearchTicketState.ENABLE)
                .groupSize(5)
                .averageElo(averageElo)
                .playerId(team.getPlayers().stream().map(User::getId).toList())
                .teamId(team.getId())
                .build();
    }

    public void matchFound() {
        if (this.state != SearchTicketState.ENABLE) {
            throw new BadRequest400("Нельзя найти матч, пока он не встал в очередь");
        }
        this.state = SearchTicketState.DISABLE;
    }

    public void cancel() {
        this.state = SearchTicketState.DISABLE;
    }

    public boolean isCompatibleWith(SearchTicket other, int maxEloDiff) {
        if (this.groupSize + other.groupSize != 10) {
            return false;
        }

        if (this.teamId.equals(other.getTeamId())) {
            return false;
        }

        return Math.abs(this.averageElo - other.getAverageElo()) <= maxEloDiff;
    }

}
