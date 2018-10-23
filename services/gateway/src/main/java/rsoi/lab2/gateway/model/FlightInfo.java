package rsoi.lab2.gateway.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class FlightInfo {

    public List<TicketInfo> listTickets;
    private int idFlight;
    private UUID uid;
    private int idRoute;
    private Timestamp dtFlight;
    private int nnTickets;
    private int maxTickets;

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }

    public List<TicketInfo> getListTickets() {
        return listTickets;
    }

    public void setListTickets(List<TicketInfo> listTickets) {
        this.listTickets = listTickets;
    }

    public int getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public Timestamp getDtFlight() {
        return dtFlight;
    }

    public void setDtFlight(Timestamp dtFlight) {
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
}
