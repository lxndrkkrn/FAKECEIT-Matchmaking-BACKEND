package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.fakeceit.Enum.TransactionBalanceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions_balance")

public class TransactionBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime localDateTime = LocalDateTime.now();

    @NotNull
    private BigDecimal deltaBalance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionBalanceType transactionBalanceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
