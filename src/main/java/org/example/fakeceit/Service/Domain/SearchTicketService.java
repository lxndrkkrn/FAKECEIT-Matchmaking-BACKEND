package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.SearchTicket;
import org.example.fakeceit.Entity.User;
import org.example.fakeceit.Repositories.SearchTicketRepository;
import org.example.fakeceit.Repositories.TeamRepository;
import org.example.fakeceit.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
@Transactional
@Validated
@RequiredArgsConstructor

public class SearchTicketService {

    private final SearchTicketRepository searchTicketRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

}
