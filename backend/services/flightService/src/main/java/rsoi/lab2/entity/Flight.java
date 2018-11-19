package rsoi.lab2.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idFlight;

    @Column(name = "uid")
    private UUID uuid;

    @Column(name = "uidRoute")
    private UUID uidRoute;

    @Column(name = "dateTime")
    private String dtFlight;

    @Column(name = "nnTickets")
    private int nnTickets;

    @Column(name = "maxTickets")
    private int maxTickets;

    public Flight() {

    }

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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUidRoute() {
        return uidRoute;
    }

    public void setUidRoute(UUID uidRoute) {
        this.uidRoute = uidRoute;
    }
}
