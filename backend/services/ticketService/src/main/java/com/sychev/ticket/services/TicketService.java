package com.sychev.ticket.services;

import com.sychev.ticket.entity.Ticket;
import com.sychev.ticket.model.TicketInfo;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    @NonNull
    List<TicketInfo> listAllByUidPassenger (UUID uidPassenger);

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

    int countAll();

    void deleteFlightTickets(UUID uidFlight);

    void rollback();
}
