package com.sychev.user.repositories;

import com.sychev.user.entity.Token;
import com.sychev.user.entity.User;
import com.sychev.user.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenType (TokenType tokenType);
    boolean existsByUserAndServiceUuidAndValue (User user, UUID serviceUuid, String value);
    void deleteAllByUserAndServiceUuid (User user, UUID serviceUuid);

    List<Token> findAllByUserAndServiceUuid(User user, UUID serviceUuid);

    Optional<Token> findByUserAndServiceUuidAndValueAndTokenType(User user, UUID serviceUuid, String value, TokenType tokenType);
}
