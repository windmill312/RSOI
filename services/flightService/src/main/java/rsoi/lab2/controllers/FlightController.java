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
public class FlightController {
    Logger logger = Logger.getLogger(FlightController.class.getName());

    @Autowired
    private FlightService flightService;

    public FlightController(FlightService service) {
        this.flightService = service;
    }

    @GetMapping(value = "/ping",
            produces = "application/json")
    public PingResponse ping() {
        logger.info("Get \"ping\" request.");
        return new PingResponse("ok");
    }


    @GetMapping(value = "/flights",
            produces = "application/json")
    public List<FlightInfo> listFlights() {
        logger.info("Get \"flights\" request.");
        return flightService.listAll();
    }

    @GetMapping(value = "/flight",
            params = "idFlight",
            produces = "application/json")
    public FlightInfo getFlight(@RequestParam int idFlight) {
        logger.info("Get \"show\" request with param (idFlight=" + idFlight + ").");
        return flightService.getFlightInfoById(idFlight);
    }

    @GetMapping(value = "/flights",
            params = "idRoute",
            produces = "application/json")
    public List<FlightInfo> getRouteFlights(@RequestParam int idRoute) {
        logger.info("Get \"routeFlights\" request with param (idRoute=" + idRoute + ").");
        return flightService.listRouteFlights(idRoute);
    }

    @PutMapping("/flight")
    public int add(@RequestBody FlightInfo flight) {
        logger.info("Get PUT request (add) with param (idRoute=" + flight.getIdRoute()
                + ", dtFlight=" + flight.getDtFlight() + ", maxTickets=" + flight.getMaxTickets() + ").");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            Date parsedDate = dateFormat.parse(flight.getDtFlight());
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            Flight newFlight = new Flight();
            if (flight.getIdFlight() != 0)
                newFlight.setIdFlight(flight.getIdFlight());
            newFlight.setIdRoute(flight.getIdRoute());
            newFlight.setDtFlight(timestamp.toString());
            newFlight.setNnTickets(0);
            newFlight.setMaxTickets(flight.getMaxTickets());
            newFlight.setUuid(UUID.randomUUID());
            flightService.saveOrUpdate(newFlight);
            return newFlight.getIdFlight();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return -1;
        }
    }

    @PatchMapping("/flight")
    public String edit(@RequestBody FlightInfo newFlight) {
        try {
            logger.info("Get PATCH request (edit) with param (idFlight=" + newFlight.getIdFlight()
                    + ", nnTickets=" + newFlight.getNnTickets() + ").");
            Flight flight = flightService.getFlightById(newFlight.getIdFlight());
            flight.setNnTickets(newFlight.getNnTickets());
            flightService.saveOrUpdate(flight);
            return "Done";
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return "Server error";
        }

    }

    @DeleteMapping("/flight")
    public String delete(@RequestBody int idFlight) {
        try {
            logger.info("Get DELETE request (flight) with param (idFlight=" + idFlight + ").");
            flightService.delete(idFlight);
            return "Done";
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return "Server error";
        }
    }

    @Transactional
    @DeleteMapping("/flights")
    public String deleteRouteFlights(@RequestBody int idRoute) {
        try {
            logger.info("Get DELETE request (flights) with param (idRoute=" + idRoute + ").");
        flightService.deleteRouteFlights(idRoute);
            return "Done";
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return "Server error";
        }
    }

}
