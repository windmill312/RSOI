package com.sychev.user.repositories;

import com.sychev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameOrEmail(String username, String Email);

    Optional<User> findByUuid(UUID uuid);

    boolean existsByUsername(String username);

    boolean existsByEmailOrUsername(String email, String username);

    boolean existsByEmail(String email);
}
