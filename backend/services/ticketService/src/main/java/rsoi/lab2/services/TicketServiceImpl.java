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
    public List<TicketInfo> listAll() {
        return ticketRepository.findAll().stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    private TicketInfo buildTicketInfo(Ticket ticket) {
        TicketInfo info = new TicketInfo();
        info.setIdTicket(ticket.getIdTicket());
        info.setClassType(ticket.getClassType());
        info.setIdPassenger(ticket.getIdPassenger());
        info.setIdFlight(ticket.getIdFlight());
        info.setUid(ticket.getUid());
        return info;
    }

    @Override
    public TicketInfo getTicketInfoByUid(UUID uid) {
        return buildTicketInfo(ticketRepository.findByUid(uid));
    }

    @Override
    public Ticket getTicketById(int idTicket) {
        return ticketRepository.findById(idTicket);
    }

    @Override
    public TicketInfo getTicketInfoById(int idTicket) {
        return buildTicketInfo(ticketRepository.findById(idTicket));
    }

    @Override
    public List<TicketInfo> listFlightTickets(int idFlight) {
        return ticketRepository.findAllByIdFlight(idFlight).stream().map(this::buildTicketInfo).collect(Collectors.toList());
    }

    @Override
    public int countTicketsByFlightAndClassType(int idFlight, String classType) {
        return ticketRepository.countTicketsByIdFlightAndAndClassType(idFlight, classType);
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
        ticketRepository.deleteByUid(uid);
    }

    @Override
    public int countFlightTickets(int idFlight) {
        return ticketRepository.countTicketsByIdFlight(idFlight);
    }

    @Override
    public void deleteFlightTickets(int id) {
        ticketRepository.deleteTicketsByIdFlight(id);
    }

}
