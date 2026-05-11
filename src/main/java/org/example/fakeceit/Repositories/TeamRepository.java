package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "SELECT * FROM teams WHERE is_search_game = true AND id != :id", nativeQuery = true)
    Optional<Team> findLookingForTeam(@Param("id") Long id);

    List<Team> findAllByIsSearchGameTrue();

}
