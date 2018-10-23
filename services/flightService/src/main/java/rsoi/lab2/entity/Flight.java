package rsoi.lab2.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFlight;
    @Column(name = "uid")
    private UUID uuid;
    @Column(name = "id_route")
    private int idRoute;
    @Column(name = "dt_tm")
    private Timestamp dtFlight;
    @Column(name = "num_tickets")
    private int nnTickets;
    @Column(name = "max_tickets")
    private int maxTickets;

    public Flight() {

    }

    public Flight(Timestamp dtFlight, int nnTickets, int maxTickets) {
        this.dtFlight = dtFlight;
        this.nnTickets = nnTickets;
        this.maxTickets = maxTickets;
    }

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
