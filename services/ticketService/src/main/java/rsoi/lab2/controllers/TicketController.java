package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.PingResponse;
import rsoi.lab2.model.TicketInfo;
import rsoi.lab2.services.TicketService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    Logger logger = Logger.getLogger(TicketController.class.getName());

    @Autowired
    private TicketService ticketService;

    @GetMapping("/ping")
    public PingResponse ping() {
        logger.info("Get \"ping\" request.");
        return new PingResponse("ok");
    }

    @GetMapping
    public List<TicketInfo> listTickets() {
        logger.info("Get \"tickets\" request.");
        List<TicketInfo> list = null;
        try {
            list = ticketService.listAll();
        }catch (Exception ex) {}
        return list;
    }

    @GetMapping(value = "/flightTickets",
            params = "idFlight")
    public List<TicketInfo> getFlightTickets(@RequestParam Integer idFlight) {
        logger.info("Get \"flightTickets\" request with param (idFlight=" + idFlight + ").");
        return ticketService.listFlightTickets(idFlight);
    }

    @GetMapping(value = "/show",
            params = "idTicket")
    public TicketInfo getTicket(@RequestParam Integer idTicket) {
        logger.info("Get \"show\" request with param (idTicket=" + idTicket + ").");
        return ticketService.getTicketInfoById(idTicket);
    }

    @GetMapping(
            value = "/add",
            params = {"classType", "idFlight", "idPassenger", "nnMaxTickets"}
    )
    public int add(@RequestParam String classType, @RequestParam int idFlight, @RequestParam int idPassenger, @RequestParam int nnMaxTickets) {
            logger.info("Get \"add\" request with params (classType=" + classType + ", idFlight=" + idFlight +
                    ", idPassenger=" + idPassenger + ").");
            int count = 0;
            try {
                count = ticketService.countFlightTickets(idFlight);
            }catch (Exception e) {
                logger.info(e.getLocalizedMessage());
            }
            logger.info("Method 'add': count=" + count);
            if (count < nnMaxTickets | count == 0) {
                Ticket ticket = new Ticket();
                ticket.setIdFlight(idFlight);
                ticket.setIdPassenger(idPassenger);
                ticket.setClassType(classType);
                ticket.setUid(UUID.randomUUID());
                try {
                    ticketService.saveOrUpdate(ticket);
                }catch (Exception e) {
                    logger.info(e.getLocalizedMessage());
                    return -2;
                }
                return count;
            } else
                return -1;
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

    @GetMapping(
            value = "/edit",
            params = {"idTicket", "classType"}
    )
    public String edit(@RequestParam int idTicket,
                       @RequestParam String classType) {
        logger.info("Get \"edit\" request with params (idTicket=" + idTicket + ", classType=" + classType + ").");
        Ticket ticket = ticketService.getTicketById(idTicket);
        ticket.setClassType(classType);
        ticketService.saveOrUpdate(ticket);
        return "Done";
    }

    @GetMapping(value = "/delete",
            params = "idTicket")
    public boolean delete(@RequestParam int idTicket) {
        logger.info("Get \"delete\" request with param (idTicket=" + idTicket + ").");
        ticketService.delete(idTicket);
        return true;
    }

    @Transactional
    @GetMapping(value = "/deleteFlightTickets",
            params = "idFlight")
    public boolean deleteFlightTickets(@RequestParam int idFlight) {
        logger.info("Get \"deleteFlightTickets\" request with param (idFlight=" + idFlight + ").");
        ticketService.deleteFlightTickets(idFlight);
        return true;
    }

}
