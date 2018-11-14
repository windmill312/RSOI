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
    public ResponseEntity getFlightTickets(@RequestParam Integer idFlight, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get \"flightTickets\" request with param (idFlight=" + idFlight + ", page=" + page + ", size=" + size + ").");
        List<TicketInfo> list = ticketService.listFlightTickets(idFlight);
        if ((size * page) > list.size())
            return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
        else
            return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
    }

    @GetMapping(value = "/ticket",
            params = "idTicket")
    public ResponseEntity getTicket(@RequestParam Integer idTicket) {
        logger.info("Get \"ticket\" request with param (idTicket=" + idTicket + ").");
        return ResponseEntity.ok(ticketService.getTicketInfoById(idTicket));
    }

    @PutMapping("/ticket")
    public ResponseEntity add(@RequestBody TicketInfo ticketInfo) {
        try {
            logger.info("Get PUT request (add) with params (classType=" + ticketInfo.getClassType() + ", idFlight=" + ticketInfo.getIdFlight() +
                    ", idPassenger=" + ticketInfo.getIdPassenger() + ").");
            Ticket ticket = new Ticket();
            if (ticketInfo.getIdTicket() != 0)
                ticket.setIdTicket(ticketInfo.getIdTicket());
            ticket.setIdFlight(ticketInfo.getIdFlight());
            ticket.setIdPassenger(ticketInfo.getIdPassenger());
            ticket.setClassType(ticketInfo.getClassType());
            ticket.setUid(UUID.randomUUID());
            ticketService.saveOrUpdate(ticket);
            return ResponseEntity.ok(ticket.getIdTicket());
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(
            value = "/countTickets",
            params = "idFlight"
    )
    public ResponseEntity countFlightTickets(@RequestParam int idFlight) {
        logger.info("Get \"countTickets\" request with param (idFlight=" + idFlight + ").");
        return ResponseEntity.ok(ticketService.countFlightTickets(idFlight));
    }

    @GetMapping(
            value = "/countTickets",
            params = {"idFlight", "classType"}
    )
    public ResponseEntity countTickets(@RequestParam int idFlight, @RequestParam String classType) {
        logger.info("Get \"countTickets\" request with params (idFlight=" + idFlight + ", classType=" + classType + ").");
        return ResponseEntity.ok(ticketService.countTicketsByFlightAndClassType(idFlight, classType));
    }

    @PatchMapping("/ticket")
    public ResponseEntity edit(@RequestBody TicketInfo ticketInfo) {
        logger.info("Get PATCH request (edit) with params (idTicket=" + ticketInfo.getIdTicket() + ", classType=" + ticketInfo.getClassType() + ").");
        Ticket ticket = ticketService.getTicketById(ticketInfo.getIdTicket());
        ticket.setClassType(ticketInfo.getClassType());
        ticketService.saveOrUpdate(ticket);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ticket")
    public ResponseEntity delete(@RequestBody int idTicket) {
        logger.info("Get DELETE request (delete) with param (idTicket=" + idTicket + ").");
        ticketService.delete(idTicket);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/tickets")
    public ResponseEntity deleteFlightTickets(@RequestBody int idFlight) {
        logger.info("Get DELETE request (deleteFlightTickets) with param (idFlight=" + idFlight + ").");
        ticketService.deleteFlightTickets(idFlight);
        return ResponseEntity.ok().build();
    }

}
