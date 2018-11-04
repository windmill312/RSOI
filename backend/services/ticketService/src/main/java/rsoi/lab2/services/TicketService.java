package rsoi.lab2.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.TicketInfo;

import java.util.List;

public interface TicketService {

    @NonNull
    List<TicketInfo> listAll();

    @Nullable
    TicketInfo getTicketInfoById(int idTicket);

    @Nullable
    List<TicketInfo> listFlightTickets(int idFlight);

    @Nullable
    int countTicketsByFlightAndClassType(int idFlight, String classType);

    @Nullable
    Ticket getTicketById(int id);

    Ticket saveOrUpdate(Ticket ticket);

    void delete(int id);

    int countFlightTickets(int idFlight);

    void deleteFlightTickets(int id);
}
