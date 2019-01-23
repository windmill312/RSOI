package com.sychev.user.repositories;

import com.sychev.user.entity.ExternalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExternalServiceRepository extends JpaRepository<ExternalService, Long> {
    Boolean existsByName(String name);
    Boolean existsByUuid(UUID uuid);
    Optional<ExternalService> findByUuid(UUID uuid);
}
