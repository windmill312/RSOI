package rsoi.lab2.services;

import org.json.JSONException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rsoi.lab2.entity.Flight;
import rsoi.lab2.model.FlightInfo;

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
