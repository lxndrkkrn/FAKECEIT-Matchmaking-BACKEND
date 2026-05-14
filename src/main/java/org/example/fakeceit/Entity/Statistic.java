package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.EmbeddedTable;

import java.math.BigDecimal;

@Entity
@Table(name = "statistics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero(message = "Количество игр должно быть не отрицательным")
    @Column(columnDefinition = "int default 0")
    private Integer countMatches;

    @PositiveOrZero(message = "Количество побед должно быть не отрицательным")
    @Column(columnDefinition = "int default 0")
    private Integer countWins;

    @PositiveOrZero(message = "Количество поражений должно быть не отрицательным")
    @Column(columnDefinition = "int default 0")
    private Integer countLoses;

    @PositiveOrZero(message = "Количество убийств должно быть не отрицательным")
    @Column(columnDefinition = "int default 0")
    private Integer countKills;

    @PositiveOrZero(message = "Количество смертей должно быть не отрицательным")
    @Column(columnDefinition = "int default 0")
    private Integer countDeaths;

    @PositiveOrZero(message = "K/D должно быть не отрицательным")
    @Column(columnDefinition = "double default 0")
    private Double KD;

    @PositiveOrZero(message = "W/R должен быть не отрицательным")
    @Column(columnDefinition = "double default 0")
    private Double WR;

    public void updateStats(int newKill, int newDeath, boolean isWin) {
        this.countMatches++;

        if (isWin) this.countWins++; else this.countLoses++;

        this.countKills += newKill;
        this.countDeaths += newDeath;

        this.KD = (countDeaths == 0) ? (double) countKills : (double) countKills / countDeaths;
        this.WR = (countMatches == 0) ? (double) WR : (double) countWins / countMatches * 100;
    }

    public void resetStats() {
        this.countMatches = 0;

        this.countWins = 0;
        this.countLoses = 0;

        this.countKills = 0;
        this.countDeaths = 0;

        this.KD = 0d;
        this.WR = 0d;
    }

}
