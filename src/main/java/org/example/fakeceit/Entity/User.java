package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.fakeceit.Enum.EloLevel;
import org.example.fakeceit.Enum.UserState;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 16, message = "Имя должно быть от 3 до 16 символов")
    private String name;

    @NotNull
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @NotNull
    @PositiveOrZero(message = "Баланс не может быть отрицательным")
    private BigDecimal balance = new BigDecimal("0");

    @NotNull
    @Min(value = 100, message = "Эло не может быть ниже 100")
    @Column(columnDefinition = "int default 1000")
    private Integer elo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EloLevel level = EloLevel.LEVEL_4;

    @NotNull
    private Boolean sub = false;

    @NotNull
    private Boolean isSearchGame = false;

    @NotNull
    private Boolean isInGame = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserState userState = UserState.ACTIVE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TransactionGame> userTransactionGameList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TransactionBalance> userTransactionBalanceList = new ArrayList<>();

    @OneToMany(mappedBy = "captain", fetch = FetchType.LAZY)
    private List<Team> captainList = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "players")
    private List<Team> teamList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "statistic_id", referencedColumnName = "id")
    private Statistic statistic;

}
