package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.SearchTicket;
import org.example.fakeceit.Enum.SearchTicketStatePlayer;
import org.example.fakeceit.Enum.SearchTicketStateTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchTicketRepository extends JpaRepository<SearchTicket, Long> {

    List<SearchTicket> findAllByStateTeam(SearchTicketStateTeam state);

    List<SearchTicket> findAllByStatePlayer(SearchTicketStatePlayer state);

}
