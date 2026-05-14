package org.example.fakeceit.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fakeceit.Enum.TransactionBalanceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions_balance")
@Builder
@NoArgsConstructor
@AllArgsConstructor

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
