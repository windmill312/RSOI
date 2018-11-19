package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

    Logger logger = Logger.getLogger(TicketController.class.getName());

    @Autowired
    private TicketService ticketService;

    public TicketController(TicketService service) {
        this.ticketService = service;
    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        logger.info("Get \"ping\" request.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tickets")
    public ResponseEntity listTickets(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get \"tickets\" request with params (page=" + page + ", size=" + size + ").");
        List<TicketInfo> list = ticketService.listAll();
        if ((size * page) > list.size())
            return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
        else
            return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
    }

    @GetMapping("/flightTickets")
    public ResponseEntity getFlightTickets(@RequestParam String uidFlight, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get \"flightTickets\" request with param (uidFlight=" + uidFlight + ", page=" + page + ", size=" + size + ").");
        List<TicketInfo> list = ticketService.listFlightTickets(UUID.fromString(uidFlight));
        if ((size * page) > list.size())
            return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
        else
            return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
    }

    @GetMapping(value = "/ticket",
            params = "uidTicket")
    public ResponseEntity getTicket(@RequestParam String uidTicket) {
        logger.info("Get \"ticket\" request with param (uidTicket=" + uidTicket + ").");
        return ResponseEntity.ok(ticketService.getTicketInfoByUid(UUID.fromString(uidTicket)));
    }

    @PutMapping("/ticket")
    public ResponseEntity add(@RequestBody TicketInfo ticketInfo) {
        try {
            logger.info("Get PUT request (add) with params (classType=" + ticketInfo.getClassType() + ", idFlight=" + ticketInfo.getUidFlight() +
                    ", idPassenger=" + ticketInfo.getIdPassenger() + ").");
            Ticket ticket = new Ticket();
            if (ticketInfo.getIdTicket() != 0)
                ticket.setIdTicket(ticketInfo.getIdTicket());
            ticket.setUidFlight(ticketInfo.getUidFlight());
            ticket.setIdPassenger(ticketInfo.getIdPassenger());
            ticket.setClassType(ticketInfo.getClassType());
            ticket.setUid(UUID.randomUUID());
            ticketService.saveOrUpdate(ticket);
            return ResponseEntity.ok(ticket.getUid().toString());
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(
            value = "/countTickets",
            params = "uidFlight"
    )
    public ResponseEntity countFlightTickets(@RequestParam UUID uidFlight) {
        logger.info("Get \"countTickets\" request with param (uidFlight=" + uidFlight + ").");
        return ResponseEntity.ok(ticketService.countFlightTickets(uidFlight));
    }

    @GetMapping(
            value = "/countTickets",
            params = {"uidFlight", "classType"}
    )
    public ResponseEntity countTickets(@RequestParam UUID uidFlight, @RequestParam String classType) {
        logger.info("Get \"countTickets\" request with params (uidFlight=" + uidFlight + ", classType=" + classType + ").");
        return ResponseEntity.ok(ticketService.countTicketsByFlightAndClassType(uidFlight, classType));
    }

    @PatchMapping("/ticket")
    public ResponseEntity edit(@RequestBody TicketInfo ticketInfo) {
        logger.info("Get PATCH request (edit) with params (uidTicket=" + ticketInfo.getUid() + ", classType=" + ticketInfo.getClassType() + ").");
        Ticket ticket = ticketService.getTicketByUid(ticketInfo.getUid());
        ticket.setClassType(ticketInfo.getClassType());
        ticketService.saveOrUpdate(ticket);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/ticket")
    public ResponseEntity delete(@RequestBody String uidTicket) {
        logger.info("Get DELETE request (delete) with param (uidTicket=" + uidTicket + ").");
        ticketService.delete(UUID.fromString(uidTicket));
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/tickets")
    public ResponseEntity deleteFlightTickets(@RequestBody String uidFlight) {
        logger.info("Get DELETE request (deleteFlightTickets) with param (idFlight=" + uidFlight + ").");
        ticketService.deleteFlightTickets(UUID.fromString(uidFlight));
        return ResponseEntity.ok().build();
    }

}
