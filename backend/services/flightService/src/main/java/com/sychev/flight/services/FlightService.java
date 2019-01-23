package com.sychev.flight.services;

import com.sychev.flight.model.FlightInfo;
import org.json.JSONException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import com.sychev.flight.entity.Flight;

import java.util.List;
import java.util.UUID;

public interface FlightService {

    @NonNull
    List<FlightInfo> listAll();

    @Nullable
    FlightInfo getFlightInfoByUid(UUID uidFlight);

    @Nullable
    List<FlightInfo> listRouteFlights(UUID uidRoute);

    Flight saveOrUpdate(Flight flight);

    void delete(UUID uidFlight);

    void deleteRouteFlights(UUID uidRoute);

    @Nullable
    Flight getFlightByUid(UUID uidFlight);

    int countAll();

    void rollback() throws JSONException;
}
