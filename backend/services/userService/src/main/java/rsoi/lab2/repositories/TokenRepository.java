package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rsoi.lab2.entity.Role;
import rsoi.lab2.entity.Token;
import rsoi.lab2.model.RoleName;
import rsoi.lab2.model.TokenType;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenType (TokenType tokenType);
}
