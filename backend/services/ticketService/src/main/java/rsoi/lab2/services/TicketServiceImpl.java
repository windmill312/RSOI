package rsoi.lab2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.TicketInfo;
import rsoi.lab2.repositories.TicketRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @NonNull
    private TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<TicketInfo> listAllByUidPassenger(UUID uidPassenger) {
        return ticketRepository.findAllByUidPassenger(uidPassenger).stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    @Override
    public List<TicketInfo> listAll() {
        return ticketRepository.findAll().stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    private TicketInfo buildTicketInfo(Ticket ticket) {
        TicketInfo info = new TicketInfo();
        info.setIdTicket(ticket.getIdTicket());
        info.setClassType(ticket.getClassType());
        info.setUidPassenger(ticket.getUidPassenger());
        info.setUidFlight(ticket.getUidFlight());
        info.setUid(ticket.getUid());
        return info;
    }

    @Override
    public TicketInfo getTicketInfoByUid(UUID uid) {
        return buildTicketInfo(ticketRepository.findByUid(uid));
    }

    @Override
    public List<TicketInfo> listFlightTickets(UUID uidFlight) {
        return ticketRepository.findAllByUidFlight(uidFlight).stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    @Override
    public int countTicketsByFlightAndClassType(UUID uidFlight, String classType) {
        return ticketRepository.countTicketsByUidFlightAndClassType(uidFlight, classType);
    }

    @Override
    public Ticket getTicketByUid(UUID uid) {
        return ticketRepository.findByUid(uid);
    }

    @Override
    public Ticket saveOrUpdate(Ticket ticket) {
        ticketRepository.save(ticket);
        return ticket;
    }

    @Override
    public void delete(UUID uid) {
        ticketRepository.deleteTicketsByUid(uid);
    }

    @Override
    public int countFlightTickets(UUID uidFlight) {
        return ticketRepository.countTicketsByUidFlight(uidFlight);
    }

    @Override
    public int countAll() {
        return ticketRepository.findAll().size();
    }

    @Override
    public void deleteFlightTickets(UUID uidFlight) {
        ticketRepository.deleteTicketsByUidFlight(uidFlight);
    }

}
