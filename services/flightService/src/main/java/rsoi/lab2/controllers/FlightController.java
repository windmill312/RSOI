package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rsoi.lab2.entity.Flight;
import rsoi.lab2.model.FlightInfo;
import rsoi.lab2.model.PingResponse;
import rsoi.lab2.services.FlightService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/flights")
public class FlightController {
    Logger logger = Logger.getLogger(FlightController.class.getName());

    @Autowired
    private FlightService flightService;

    @GetMapping(value = "/ping",
            produces = "application/json")
    public PingResponse ping() {
        logger.info("Get \"ping\" request.");
        return new PingResponse("ok");
    }

    @GetMapping(produces = "application/json")
    public List<FlightInfo> listFlights() {
        logger.info("Get \"flights\" request.");
        return flightService.listAll();
    }

    @GetMapping(value = "/show",
            params = "idFlight",
            produces = "application/json")
    public FlightInfo getFlight(@RequestParam int idFlight) {
        logger.info("Get \"show\" request with param (idFlight=" + idFlight + ").");
        return flightService.getFlightInfoById(idFlight);
    }

    @GetMapping(value = "/routeFlights",
            params = "idRoute",
            produces = "application/json")
    public List<FlightInfo> getRouteFlights(@RequestParam int idRoute) {
        logger.info("Get \"routeFlights\" request with param (idRoute=" + idRoute + ").");
        return flightService.listRouteFlights(idRoute);
    }

    @GetMapping(
            value = "/add",
            params = {"idRoute", "dtFlight", "maxTickets"}
    )
    public String add(@RequestParam int idRoute, @RequestParam String dtFlight, @RequestParam int maxTickets) {
        logger.info("Get \"add\" request with param (idRoute=" + idRoute + ", dtFlight=" + dtFlight + ", maxTickets=" + maxTickets + ").");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            Date parsedDate = dateFormat.parse(dtFlight);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            Flight flight = new Flight();
            flight.setIdRoute(idRoute);
            flight.setDtFlight(timestamp);
            flight.setNnTickets(0);
            flight.setMaxTickets(maxTickets);
            flight.setUuid(UUID.randomUUID());
            flightService.saveOrUpdate(flight);
        } catch (Exception e) {
        }

        return "Done";

    }

    @GetMapping(
            value = "/edit",
            params = {"idFlight", "nnTickets"}
    )
    public boolean edit(@RequestParam int idFlight,
                        @RequestParam int nnTickets) {
        logger.info("Get \"edit\" request with param (idFlight=" + idFlight + ", nnTickets=" + nnTickets + ").");
        Flight flight = flightService.getFlightById(idFlight);
        flight.setMaxTickets(nnTickets);
        flightService.saveOrUpdate(flight);
        return true;
    }

    @GetMapping(value = "/delete",
            params = "idFlight")
    public boolean delete(@RequestParam int idFlight) {
        logger.info("Get \"delete\" request with param (idFlight=" + idFlight + ").");
        flightService.delete(idFlight);
        return true;
    }

    @Transactional
    @GetMapping(value = "/deleteRouteFlights",
            params = "idRoute")
    public boolean deleteRouteFlights(@RequestParam int idRoute) {
        logger.info("Get \"deleteRouteFlights\" request with param (idRoute=" + idRoute + ").");
        flightService.deleteRouteFlights(idRoute);
        return true;
    }

}
