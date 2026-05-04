package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MapRepository extends JpaRepository<Map, Long> {

    Optional<Map> findByName(String name);

    boolean existByName(String name);
}
