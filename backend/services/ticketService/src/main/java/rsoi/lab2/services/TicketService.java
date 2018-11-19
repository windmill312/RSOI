package rsoi.lab2.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.TicketInfo;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    @NonNull
    List<TicketInfo> listAll();

    @Nullable
    TicketInfo getTicketInfoByUid(UUID uid);

    @Nullable
    Ticket getTicketById(int idTicket);

    @Nullable
    TicketInfo getTicketInfoById(int idTicket);

    @Nullable
    List<TicketInfo> listFlightTickets(int idFlight);

    @Nullable
    int countTicketsByFlightAndClassType(int idFlight, String classType);

    @Nullable
    Ticket getTicketByUid(UUID uid);

    Ticket saveOrUpdate(Ticket ticket);

    void delete(UUID uid);

    int countFlightTickets(int idFlight);

    void deleteFlightTickets(int idFlight);
}
