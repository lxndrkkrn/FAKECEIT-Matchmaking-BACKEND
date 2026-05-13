package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {


}
