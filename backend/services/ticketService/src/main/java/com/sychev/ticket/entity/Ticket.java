package com.sychev.ticket.entity;

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
    @Column(name = "uidPassenger")
    private UUID uidPassenger;
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

    public UUID getUidPassenger() {
        return uidPassenger;
    }

    public void setUidPassenger(UUID uidPassenger) {
        this.uidPassenger = uidPassenger;
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
