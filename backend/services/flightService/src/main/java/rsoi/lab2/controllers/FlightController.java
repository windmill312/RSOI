package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.google.gson.Gson;

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
    public ResponseEntity<PingResponse> ping() {
        logger.info("Get \"ping\" request.");
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/flights",
            produces = "application/json")
    public ResponseEntity<List<FlightInfo>> listFlights(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get \"flights\" request with params (page=" + page + ", size=" + size + ").");
        List<FlightInfo> list = flightService.listAll();
        if ((size * page) > list.size())
            return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
        else
            return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
    }

    @GetMapping(value = "/flight",
            params = "idFlight",
            produces = "application/json")
    public ResponseEntity<FlightInfo> getFlight(@RequestParam int idFlight) {
        logger.info("Get \"show\" request with param (idFlight=" + idFlight + ").");
        return ResponseEntity.ok(flightService.getFlightInfoById(idFlight));
    }

    @GetMapping(value = "/flights",
            params = "idRoute",
            produces = "application/json")
    public ResponseEntity<List<FlightInfo>> getRouteFlights(@RequestParam int idRoute, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get \"routeFlights\" request with param (idRoute=" + idRoute + ", page=" + page + ", size=" + size + ").");
        List<FlightInfo> list = flightService.listRouteFlights(idRoute);
        if ((size * page) > list.size())
            return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
        else
            return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
    }

    @PutMapping("/flight")
    public ResponseEntity<String> add(@RequestBody FlightInfo flight) {
        logger.info("Get PUT request (add) with param (idRoute=" + flight.getIdRoute()
                + ", dtFlight=" + flight.getDtFlight() + ", maxTickets=" + flight.getMaxTickets() + ").");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
            return new ResponseEntity<>(new Gson().toJson(newFlight.getIdFlight()), HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/flight")
    public ResponseEntity edit(@RequestBody FlightInfo newFlight) {
        try {
            logger.info("Get PATCH request (edit) with param (idFlight=" + newFlight.getIdFlight()
                    + ", nnTickets=" + newFlight.getNnTickets() + ").");
            Flight flight = flightService.getFlightById(newFlight.getIdFlight());
            flight.setNnTickets(newFlight.getNnTickets());
            flightService.saveOrUpdate(flight);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/flight")
    public ResponseEntity delete(@RequestBody int idFlight) {
        try {
            logger.info("Get DELETE request (flight) with param (idFlight=" + idFlight + ").");
            flightService.delete(idFlight);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Transactional
    @DeleteMapping("/flights")
    public ResponseEntity deleteRouteFlights(@RequestBody int idRoute) {
        try {
            logger.info("Get DELETE request (flights) with param (idRoute=" + idRoute + ").");
            flightService.deleteRouteFlights(idRoute);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
