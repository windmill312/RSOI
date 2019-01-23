package com.sychev.flight.repositories;

import com.sychev.flight.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    void deleteFlightsByUidRoute(UUID uidRoute);

    List<Flight> findAllByUidRoute(UUID uidRoute);

    Flight findByUuid(UUID uidFlight);

    void deleteByUuid(UUID uidFlight);

}
