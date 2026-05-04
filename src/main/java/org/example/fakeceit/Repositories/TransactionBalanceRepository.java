package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.TransactionBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionBalanceRepository extends JpaRepository<TransactionBalance, Long> {
}
