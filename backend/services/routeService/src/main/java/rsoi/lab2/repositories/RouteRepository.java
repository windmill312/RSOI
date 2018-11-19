package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rsoi.lab2.entity.Route;

import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findAllByNmRoute(String nmRoute);

    Route findByUid(UUID uidRoute);

    void deleteByUid(UUID uidRoute);
}
