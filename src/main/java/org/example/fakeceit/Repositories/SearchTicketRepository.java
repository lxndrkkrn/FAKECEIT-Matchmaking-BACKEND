package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.SearchTicket;
import org.example.fakeceit.Enum.SearchTicketState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchTicketRepository extends JpaRepository<SearchTicket, Long> {

    List<SearchTicket> findAllByState(SearchTicketState state);

}
