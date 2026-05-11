package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.GameMap;
import org.example.fakeceit.Entity.IP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IPRepository extends JpaRepository<IP, Long> {

    @Query("SELECT i FROM IP i WHERE i.id NOT IN " +
            "(SELECT bi.id FROM Lobby l JOIN l.bannedMaps bi WHERE l.id = :lobbyId)")
    IP findRemainingIp(@Param("lobbyId") Long id);

}
