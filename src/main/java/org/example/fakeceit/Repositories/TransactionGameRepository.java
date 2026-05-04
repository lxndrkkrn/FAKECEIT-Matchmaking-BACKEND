package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.TransactionGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionGameRepository extends JpaRepository<TransactionGame, Long> {
}
