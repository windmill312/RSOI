package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rsoi.lab2.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameOrEmail(String username, String Email);

    Optional<User> findByUuid(UUID uuid);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
