package com.sychev.route.repositories;

import com.sychev.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findAllByNmRoute(String nmRoute);

    Route findByUid(UUID uidRoute);

    void deleteByUid(UUID uidRoute);
}
