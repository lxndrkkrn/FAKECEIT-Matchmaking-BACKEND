package org.example.fakeceit.Repositories;

import jakarta.persistence.LockModeType;
import org.example.fakeceit.Entity.Lobby;
import org.example.fakeceit.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "SELECT * FROM teams WHERE is_search_game = true AND id != :id", nativeQuery = true)
    Optional<Team> findLookingForTeam(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Team t WHERE t.isSearchGame = true")
    List<Team> findLastTeamByPlayerId();

    @Query("SELECT t FROM Team t JOIN t.players p WHERE p.id = :userId ORDER BY t.id DESC")
    Optional<Team> findFirstByPlayerId(@Param("userId") Long userId);

}
