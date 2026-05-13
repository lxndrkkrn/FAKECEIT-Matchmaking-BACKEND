package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MapRepository extends JpaRepository<GameMap, Long> {

    Optional<GameMap> findByName(String name);

    boolean existsByName(String name);

    List<GameMap> findByIsActive(Boolean isActive);

    @Query("SELECT m FROM GameMap m WHERE m.id NOT IN " +
            "(SELECT bm.id FROM Lobby l JOIN l.bannedMaps bm WHERE l.id = :lobbyId)")
    GameMap findRemainingMap(@Param("lobbyId") Long id);
}
