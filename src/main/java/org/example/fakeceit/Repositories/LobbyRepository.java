package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {
}
