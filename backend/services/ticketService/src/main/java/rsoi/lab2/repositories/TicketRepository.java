package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import rsoi.lab2.entity.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    int countTicketsByUidFlight(UUID uidFlight);

    void deleteTicketsByUidFlight(UUID uidFlight);

    List<Ticket> findAllByUidFlight(UUID uidFlight);

    int countTicketsByUidFlightAndClassType(UUID uidFlight, String classType);

    void deleteByUid(UUID uid);

    Ticket findByUid(UUID uid);

    Ticket findById(int idTicket);
}
