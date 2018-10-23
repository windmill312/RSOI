package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rsoi.lab2.entity.Route;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findAllByNmRoute(String nmRoute);

}
