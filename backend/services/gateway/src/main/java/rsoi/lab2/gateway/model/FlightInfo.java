package rsoi.lab2.gateway.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlightInfo {

    private int idFlight;

    private UUID uid;

    private UUID uidRoute;

    private String dtFlight;

    private int nnTickets;

    private int maxTickets;

    private List<TicketInfo> tickets;

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }

    public String getDtFlight() {
        return dtFlight;
    }

    public void setDtFlight(String dtFlight) {
        this.dtFlight = dtFlight;
    }

    public int getNnTickets() {
        return nnTickets;
    }

    public void setNnTickets(int nnTickets) {
        this.nnTickets = nnTickets;
    }

    public int getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public UUID getUidRoute() {
        return uidRoute;
    }

    public void setUidRoute(UUID uidRoute) {
        this.uidRoute = uidRoute;
    }

    public List<TicketInfo> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketInfo> tickets) {
        this.tickets = tickets;
    }
}