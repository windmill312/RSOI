package com.sychev.user.repositories;

import com.sychev.user.entity.ServiceKey;
import com.sychev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sychev.user.entity.ExternalService;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceKeyRepository extends JpaRepository<ServiceKey, Long> {

    void deleteAllByUserAndService(User user, ExternalService service);

    Optional<ServiceKey> findByValue(UUID value);
}
