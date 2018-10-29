package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.PingResponse;
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
    public PingResponse ping() {
        logger.info("Get \"ping\" request.");
        return new PingResponse("ok");
    }

    @GetMapping("/tickets")
    public List<TicketInfo> listTickets() {
        List<TicketInfo> list = null;
        try {
            logger.info("Get \"tickets\" request.");
            list = ticketService.listAll();
        } catch (Exception ex) {
        }
        return list;
    }

    @GetMapping(value = "/flightTickets",
            params = "idFlight")
    public List<TicketInfo> getFlightTickets(@RequestParam Integer idFlight) {
        logger.info("Get \"flightTickets\" request with param (idFlight=" + idFlight + ").");
        return ticketService.listFlightTickets(idFlight);
    }

    @GetMapping(value = "/ticket",
            params = "idTicket")
    public TicketInfo getTicket(@RequestParam Integer idTicket) {
        logger.info("Get \"ticket\" request with param (idTicket=" + idTicket + ").");
        return ticketService.getTicketInfoById(idTicket);
    }

    @PutMapping("/ticket")
    public int add(@RequestBody TicketInfo ticketInfo) {
        try {
            logger.info("Get PUT request (add) with params (classType=" + ticketInfo.getClassType() + ", idFlight=" + ticketInfo.getIdFlight() +
                    ", idPassenger=" + ticketInfo.getIdPassenger() + ").");
            Ticket ticket = new Ticket();
            ticket.setIdFlight(ticketInfo.getIdFlight());
            ticket.setIdPassenger(ticketInfo.getIdPassenger());
            ticket.setClassType(ticketInfo.getClassType());
            ticket.setUid(UUID.randomUUID());
            ticketService.saveOrUpdate(ticket);
            return ticket.getIdTicket();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return -1;
        }
    }

    @GetMapping(
            value = "/countTickets",
            params = "idFlight"
    )
    public int countFlightTickets(@RequestParam int idFlight) {
        logger.info("Get \"countTickets\" request with param (idFlight=" + idFlight + ").");
        return ticketService.countFlightTickets(idFlight);
    }

    @GetMapping(
            value = "/countTickets",
            params = {"idFlight", "classType"}
    )
    public int countTickets(@RequestParam int idFlight, @RequestParam String classType) {
        logger.info("Get \"countTickets\" request with params (idFlight=" + idFlight + ", classType=" + classType + ").");
        return ticketService.countTicketsByFlightAndClassType(idFlight, classType);
    }

    @PatchMapping("/ticket")
    public String edit(@RequestBody TicketInfo ticketInfo) {
        logger.info("Get PATCH request (/edit) with params (idTicket=" + ticketInfo.getIdTicket() + ", classType=" + ticketInfo.getClassType() + ").");
        Ticket ticket = ticketService.getTicketById(ticketInfo.getIdTicket());
        ticket.setClassType(ticketInfo.getClassType());
        ticketService.saveOrUpdate(ticket);
        return "Done";
    }

    @DeleteMapping("/ticket")
    public String delete(@RequestBody int idTicket) {
        logger.info("Get DELETE request (delete) with param (idTicket=" + idTicket + ").");
        ticketService.delete(idTicket);
        return "Done";
    }

    @Transactional
    @DeleteMapping(value = "/tickets")
    public String deleteFlightTickets(@RequestBody int idFlight) {
        logger.info("Get DELETE request (deleteFlightTickets) with param (idFlight=" + idFlight + ").");
        ticketService.deleteFlightTickets(idFlight);
        return "Done";
    }

}
