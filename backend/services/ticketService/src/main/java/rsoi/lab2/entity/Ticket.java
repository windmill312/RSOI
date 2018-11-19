package rsoi.lab2.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTicket;
    @Column(name = "uidFlight")
    private UUID uidFlight;
    @Column(name = "idPassenger")
    private int idPassenger;
    @Column(name = "class")
    private String classType;
    @Column
    private UUID uid;

    public Ticket() {

    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public int getIdPassenger() {
        return idPassenger;
    }

    public void setIdPassenger(int idPassenger) {
        this.idPassenger = idPassenger;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public UUID getUidFlight() {
        return uidFlight;
    }

    public void setUidFlight(UUID uidFlight) {
        this.uidFlight = uidFlight;
    }
}
