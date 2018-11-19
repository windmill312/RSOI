package rsoi.lab2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rsoi.lab2.entity.Flight;
import rsoi.lab2.model.FlightInfo;
import rsoi.lab2.repositories.FlightRepository;

import java.util.List;
import java.util.UUID;
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
        flightRepository.deleteByUuid(uidFlight);
    }

    @Override
    public void deleteRouteFlights(UUID uidRoute) {
        flightRepository.deleteFlightsByUidRoute(uidRoute);
    }

}
