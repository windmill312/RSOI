package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rsoi.lab2.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByLogin(String login);

    User findByUid(UUID uid);

    User findByToken(UUID token);

    User findByRefreshToken(UUID refreshToken);
}
