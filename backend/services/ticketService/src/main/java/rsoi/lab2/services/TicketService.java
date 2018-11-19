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
    List<TicketInfo> listFlightTickets(UUID uidFlight);

    @Nullable
    int countTicketsByFlightAndClassType(UUID uidFlight, String classType);

    @Nullable
    Ticket getTicketByUid(UUID uid);

    Ticket saveOrUpdate(Ticket ticket);

    void delete(UUID uid);

    int countFlightTickets(UUID uidFlight);

    void deleteFlightTickets(UUID uidFlight);
}
