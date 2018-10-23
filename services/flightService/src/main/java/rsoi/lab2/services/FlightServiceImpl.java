package rsoi.lab2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rsoi.lab2.entity.Flight;
import rsoi.lab2.model.FlightInfo;
import rsoi.lab2.repositories.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

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
        info.setIdRoute(flight.getIdRoute());
        info.setDtFlight(flight.getDtFlight());
        info.setMaxTickets(flight.getMaxTickets());
        info.setNnTickets(flight.getNnTickets());
        info.setUid(flight.getUuid());
        return info;
    }

    @Override
    public Flight getFlightById(int id) {
        return flightRepository.findById(id).orElse(null);
    }

    @Override
    public FlightInfo getFlightInfoById(int idFlight) {
        return flightRepository.findById(idFlight).map(this::buildTicketInfo).orElse(null);
    }

    @Override
    public List<FlightInfo> listRouteFlights(int idRoute) {
        return flightRepository.findAllByIdRoute(idRoute).stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    @Override
    public Flight saveOrUpdate(Flight flight) {
        flightRepository.save(flight);
        return flight;
    }

    @Override
    public void delete(int id) {
        flightRepository.deleteById(id);
    }

    @Override
    public void deleteRouteFlights(int id) {
        flightRepository.deleteFlightsByIdRoute(id);
    }

}
