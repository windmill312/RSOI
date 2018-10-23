package rsoi.lab2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import rsoi.lab2.entity.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @NonNull
    int countTicketsByIdFlight(int idFlight);

    void deleteTicketsByIdFlight(int idFlight);

    @NonNull
    List<Ticket> findAllByIdFlight(int idFlight);

    @NonNull
    int countTicketsByIdFlightAndAndClassType(int idFlight, String classType);


}
