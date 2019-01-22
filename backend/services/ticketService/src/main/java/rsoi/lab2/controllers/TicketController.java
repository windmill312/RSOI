package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.TicketInfo;
import rsoi.lab2.services.TicketService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class TicketController {

    private Logger logger = Logger.getLogger(TicketController.class.getName());

    @Value("${app.gatewayUuid}")
    private String gateway;

    @Autowired
    private TicketService ticketService;

    public TicketController(TicketService service) {
        this.ticketService = service;
    }

    @GetMapping(
            value = "/ping",
            params = {
            "gatewayUuid"
    })
    public ResponseEntity ping(@RequestParam String gatewayUuid) {
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
            })
    public ResponseEntity countAll(@RequestParam String gatewayUuid) {
        logger.info("Get \"countTickets\" request.");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(ticketService.countAll());
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/tickets",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity listTickets(@RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                      @RequestParam String gatewayUuid) {
        logger.info("Get \"tickets\" request with params (page=" + page + ", size=" + size + ").");
        if (gatewayUuid.equals(gateway)) {
            List<TicketInfo> list = ticketService.listAll();
            if ((size * page) > list.size())
                return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
            else
                return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/userTickets",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity listUserTickets(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "size", defaultValue = "5") int size, String userUuid,
                                          @RequestParam String gatewayUuid) {
        logger.info("Get \"user tickets\" request with params (page=" + page + ", size=" + size + "user=" + userUuid + ").");
        if (gatewayUuid.equals(gateway)) {
            List<TicketInfo> list = ticketService.listAllByUidPassenger(UUID.fromString(userUuid));
            if ((size * page) > list.size())
                return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
            else
                return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/flightTickets",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity getFlightTickets(@RequestParam String uidFlight,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "5") int size,
                                           @RequestParam String gatewayUuid) {
        logger.info("Get \"flightTickets\" request with param (uidFlight=" + uidFlight + ", page=" + page + ", size=" + size + ").");
        if (gatewayUuid.equals(gateway)) {
            List<TicketInfo> list = ticketService.listFlightTickets(UUID.fromString(uidFlight));
            if ((size * page) > list.size())
                return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
            else
                return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = "/ticket",
            params = {
            "uidTicket",
            "gatewayUuid"
            })
    public ResponseEntity getTicket(@RequestParam String uidTicket,
                                    @RequestParam String gatewayUuid) {
        logger.info("Get \"ticket\" request with param (uidTicket=" + uidTicket + ").");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(ticketService.getTicketInfoByUid(UUID.fromString(uidTicket)));
        else
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
                ticketService.rollback();
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @PutMapping(
            value = "/ticket",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity add(@RequestBody TicketInfo ticketInfo,
                              @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get PUT request (add) with params (classType=" + ticketInfo.getClassType() + ", uidFlight=" + ticketInfo.getUidFlight() +
                        ", uidPassenger=" + ticketInfo.getUidPassenger() + ").");
                Ticket ticket = new Ticket();
                if (ticketInfo.getIdTicket() != 0)
                    ticket.setIdTicket(ticketInfo.getIdTicket());
                ticket.setUidFlight(ticketInfo.getUidFlight());
                ticket.setUidPassenger(ticketInfo.getUidPassenger());
                ticket.setClassType(ticketInfo.getClassType());
                ticket.setUid(UUID.randomUUID());
                ticketService.saveOrUpdate(ticket);
                return ResponseEntity.ok(ticket.getUid().toString());
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/countTickets",
            params = {
                "uidFlight",
                "gatewayUuid"
            }
    )
    public ResponseEntity countFlightTickets(@RequestParam UUID uidFlight,
                                             @RequestParam String gatewayUuid) {
        logger.info("Get \"countTickets\" request with param (uidFlight=" + uidFlight + ").");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(ticketService.countFlightTickets(uidFlight));
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
        value = "/countTickets",
        params = {
                "uidFlight",
                "classType",
                "gatewayUuid"
        }
    )
    public ResponseEntity countTickets(@RequestParam UUID uidFlight,
                                       @RequestParam String classType,
                                       @RequestParam String gatewayUuid) {
        logger.info("Get \"countTickets\" request with params (uidFlight=" + uidFlight + ", classType=" + classType + ").");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(ticketService.countTicketsByFlightAndClassType(uidFlight, classType));
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping(
            value = "/ticket",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity edit(@RequestBody TicketInfo ticketInfo,
                               @RequestParam String gatewayUuid) {
        logger.info("Get PATCH request (edit) with params (uidTicket=" + ticketInfo.getUid() + ", classType=" + ticketInfo.getClassType() + ").");
        if (gatewayUuid.equals(gateway)) {
            Ticket ticket = ticketService.getTicketByUid(ticketInfo.getUid());
            ticket.setClassType(ticketInfo.getClassType());
            ticketService.saveOrUpdate(ticket);
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Transactional
    @DeleteMapping(
            value = "/ticket",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity delete(@RequestBody String uidTicket,
                                 @RequestParam String gatewayUuid) {
        logger.info("Get DELETE request (delete) with param (uidTicket=" + uidTicket + ").");
        if (gatewayUuid.equals(gateway)) {
            ticketService.delete(UUID.fromString(uidTicket));
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Transactional
    @DeleteMapping(
            value = "/tickets",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity deleteFlightTickets(@RequestBody String uidFlight,
                                              @RequestParam String gatewayUuid) {
        logger.info("Get DELETE request (deleteFlightTickets) with param (idFlight=" + uidFlight + ").");
            if (gatewayUuid.equals(gateway)) {
                ticketService.deleteFlightTickets(UUID.fromString(uidFlight));
                return ResponseEntity.ok().build();
            }
            else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
