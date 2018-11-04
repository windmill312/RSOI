package rsoi.lab2.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rsoi.lab2.entity.Flight;
import rsoi.lab2.model.FlightInfo;

import java.util.List;

public interface FlightService {

    @NonNull
    List<FlightInfo> listAll();

    @Nullable
    FlightInfo getFlightInfoById(int idFlight);

    @Nullable
    List<FlightInfo> listRouteFlights(int idRoute);

    Flight saveOrUpdate(Flight flight);

    void delete(int id);

    void deleteRouteFlights(int id);

    @Nullable
    Flight getFlightById(int id);
}
