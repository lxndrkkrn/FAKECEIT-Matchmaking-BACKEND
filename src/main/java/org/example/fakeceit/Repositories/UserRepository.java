package org.example.fakeceit.Repositories;

import org.example.fakeceit.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    Optional<User> findUserByName(String name);

}
