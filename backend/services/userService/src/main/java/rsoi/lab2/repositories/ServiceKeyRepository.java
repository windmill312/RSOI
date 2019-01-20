package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rsoi.lab2.entity.ExternalService;
import rsoi.lab2.entity.ServiceKey;
import rsoi.lab2.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceKeyRepository extends JpaRepository<ServiceKey, Long> {

    void deleteAllByUserAndService(User user, ExternalService service);

    Optional<ServiceKey> findByValue(UUID value);
}
