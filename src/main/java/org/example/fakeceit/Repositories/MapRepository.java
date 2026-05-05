package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MapRepository extends JpaRepository<GameMap, Long> {

    Optional<GameMap> findByName(String name);

    boolean existByName(String name);
}
