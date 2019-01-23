package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private Logger logger = Logger.getLogger(FlightController.class.getName());

    @Value("${app.gatewayUuid}")
    public String gateway;

    @Autowired
    private FlightService flightService;

    public FlightController(FlightService service) {
        this.flightService = service;
    }

    @GetMapping(
            value = "/ping",
            params = {
                    "gatewayUuid"
            },
            produces = "application/json"
    )
    public ResponseEntity<PingResponse> ping(@RequestParam String gatewayUuid) {
        logger.info("Get \"ping\" request.");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/countAll",
            params = {
                    "gatewayUuid"
            }
    )
    public ResponseEntity countAll(@RequestParam String gatewayUuid) {
        logger.info("Get \"countRoutes\" request.");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(flightService.countAll());
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/flights",
            params = {
                    "gatewayUuid"
            },
            produces = "application/json")
    public ResponseEntity<List<FlightInfo>> listFlights(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size, @RequestParam String gatewayUuid) {
        logger.info("Get \"flights\" request with params (page=" + page + ", size=" + size + ").");
        if (gatewayUuid.equals(gateway)) {
            List<FlightInfo> list = flightService.listAll();
            if ((size * page) > list.size())
                return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
            else
                return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = "/flight",
            params = {
                    "uidFlight",
                    "gatewayUuid"
            },
            produces = "application/json")
    public ResponseEntity<FlightInfo> getFlight(@RequestParam String uidFlight,
                                                @RequestParam String gatewayUuid) {
        logger.info("Get \"show\" request with param (uidFlight=" + uidFlight + ").");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(flightService.getFlightInfoByUid(UUID.fromString(uidFlight)));
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = "/flights",
            params = {
                    "uidRoute",
                    "gatewayUuid"
            },
            produces = "application/json")
    public ResponseEntity<List<FlightInfo>> getRouteFlights(@RequestParam String uidRoute,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "size", defaultValue = "5") int size,
                                                            @RequestParam String gatewayUuid) {
        logger.info("Get \"routeFlights\" request with param (uidRoute=" + uidRoute + ", page=" + page + ", size=" + size + ").");
        if (gatewayUuid.equals(gateway)) {
            List<FlightInfo> list = flightService.listRouteFlights(UUID.fromString(uidRoute));
            if ((size * page) > list.size())
                return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
            else
                return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(
            value = "/rollback",
            params = {
                    "gatewayUuid"
            }
    )
    public ResponseEntity rollback(@RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get POST request (rollback)");
                flightService.rollback();
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping(
            value = "/flight",
            params = {
                    "gatewayUuid"
            }
    )
    public ResponseEntity add(@RequestBody FlightInfo flight,
                              @RequestParam String gatewayUuid) {
        logger.info("Get PUT request (add) with param (uidRoute=" + flight.getUidRoute()
                + ", dtFlight=" + flight.getDtFlight() + ", maxTickets=" + flight.getMaxTickets() + ").");
        if (gatewayUuid.equals(gateway))
            try {
                /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date parsedDate = dateFormat.parse(flight.getDtFlight());
                Timestamp timestamp = new Timestamp(parsedDate.getTime());*/
                Flight newFlight = new Flight();
                if (flight.getIdFlight() != 0)
                    newFlight.setIdFlight(flight.getIdFlight());
                newFlight.setUidRoute(flight.getUidRoute());
                newFlight.setDtFlight(flight.getDtFlight());
                newFlight.setNnTickets(0);
                newFlight.setMaxTickets(flight.getMaxTickets());
                newFlight.setUuid(UUID.randomUUID());
                flightService.saveOrUpdate(newFlight);
                return ResponseEntity.ok(newFlight.getUuid());
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping(
            value = "/flight",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity edit(@RequestBody FlightInfo newFlight,
                               @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get PATCH request (edit) with param (uidFlight=" + newFlight.getUid()
                        + ", nnTickets=" + newFlight.getNnTickets() + ").");
                Flight flight = flightService.getFlightByUid(newFlight.getUid());
                flight.setNnTickets(newFlight.getNnTickets());
                flightService.saveOrUpdate(flight);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Transactional
    @DeleteMapping(
            value = "/flight",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity delete(@RequestBody String uidFlight,
                                 @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get DELETE request (flight) with param (uidFlight=" + uidFlight + ").");
                flightService.delete(UUID.fromString(uidFlight));
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Transactional
    @DeleteMapping(
            value = "/flights",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity deleteRouteFlights(@RequestBody String uidRoute,
                                             @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get DELETE request (flights) with param (uidRoute=" + uidRoute + ").");
                flightService.deleteRouteFlights(UUID.fromString(uidRoute));
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
