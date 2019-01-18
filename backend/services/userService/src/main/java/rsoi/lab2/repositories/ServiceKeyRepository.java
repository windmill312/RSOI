package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rsoi.lab2.entity.ServiceKey;

@Repository
public interface ServiceKeyRepository extends JpaRepository<ServiceKey, Long> {
}
