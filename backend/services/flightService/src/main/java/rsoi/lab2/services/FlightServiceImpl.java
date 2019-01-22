package rsoi.lab2.services;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rsoi.lab2.entity.Flight;
import rsoi.lab2.model.FlightInfo;
import rsoi.lab2.repositories.FlightRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    private JSONArray jsonArray = new JSONArray();

    @Autowired
    public FlightServiceImpl(FlightRepository ticketRepository) {
        this.flightRepository = ticketRepository;
    }

    @Override
    public List<FlightInfo> listAll() {
        return flightRepository.findAll().stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    private FlightInfo buildTicketInfo(Flight flight) {

        FlightInfo info = new FlightInfo();
        info.setIdFlight(flight.getIdFlight());
        info.setUidRoute(flight.getUidRoute());
        info.setDtFlight(flight.getDtFlight());
        info.setMaxTickets(flight.getMaxTickets());
        info.setNnTickets(flight.getNnTickets());
        info.setUid(flight.getUuid());
        return info;
    }

    @Override
    public Flight getFlightByUid(UUID uidFlight) {
        return flightRepository.findByUuid(uidFlight);
    }

    @Override
    public int countAll() {
        return flightRepository.findAll().size();
    }

    @Override
    public void rollback() throws JSONException {
        Gson gson = new Gson();
        try {
            for (int i=0; i< jsonArray.length(); i++)
                for (int j=0; j < jsonArray.getJSONArray(i).length(); j++ ) {
                    Flight ticket = gson.fromJson(jsonArray.getJSONArray(i).getJSONObject(j).toString(), Flight.class);
                    flightRepository.save(ticket);
                }

            jsonArray = new JSONArray();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FlightInfo getFlightInfoByUid(UUID uidFlight) {
        return buildTicketInfo(flightRepository.findByUuid(uidFlight));
    }

    @Override
    public List<FlightInfo> listRouteFlights(UUID uidRoute) {
        return flightRepository.findAllByUidRoute(uidRoute).stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    @Override
    public Flight saveOrUpdate(Flight flight) {
        flightRepository.save(flight);
        return flight;
    }

    @Override
    public void delete(UUID uidFlight) {
        jsonArray.put(flightRepository.findByUuid(uidFlight));
        flightRepository.deleteByUuid(uidFlight);
    }

    @Override
    public void deleteRouteFlights(UUID uidRoute) {
        jsonArray.put(flightRepository.findAllByUidRoute(uidRoute));
        flightRepository.deleteFlightsByUidRoute(uidRoute);
    }

}
