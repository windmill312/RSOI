package rsoi.lab2.gateway.model;

import java.util.UUID;

public class TicketInfo {

    private int idTicket;

    private UUID uidFlight;

    private int idPassenger;

    private String classType;

    private UUID uid;

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdPassenger() {
        return idPassenger;
    }

    public void setIdPassenger(int idPassenger) {
        this.idPassenger = idPassenger;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
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
