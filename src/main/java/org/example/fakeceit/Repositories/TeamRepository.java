package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
