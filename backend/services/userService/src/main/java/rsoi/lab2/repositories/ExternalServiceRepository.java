package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rsoi.lab2.entity.ExternalService;
import rsoi.lab2.entity.Role;
import rsoi.lab2.model.RoleName;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExternalServiceRepository extends JpaRepository<ExternalService, Long> {
    Boolean existsByName(String name);
    Boolean existsByUuid(UUID uuid);
}
