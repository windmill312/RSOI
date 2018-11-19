package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rsoi.lab2.entity.Flight;

import java.util.List;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    void deleteFlightsByUidRoute(UUID uidRoute);

    List<Flight> findAllByUidRoute(UUID uidRoute);

    Flight findByUid(UUID uidFlight);

    void deleteByUid (UUID uidFlight);

}
