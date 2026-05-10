package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    void dle(Team team);
}
