package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rsoi.lab2.entity.Flight;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    void deleteFlightsByIdRoute(int idRoute);

    List<Flight> findAllByIdRoute(int idRoute);

}
