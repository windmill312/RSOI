package rsoi.lab2.model;

import java.util.UUID;

public class TicketInfo {

    private int idTicket;

    private UUID uidFlight;

    private UUID uidPassenger;

    private String classType;

    private UUID uid;

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public UUID getUidPassenger() {
        return uidPassenger;
    }

    public void setUidPassenger(UUID uidPassenger) {
        this.uidPassenger = uidPassenger;
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
