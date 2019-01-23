package com.sychev.route.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idRoute;

    @Column(name = "nmRoute")
    private String nmRoute;

    @Column
    private UUID uid;

    public Route() {

    }

    public Route(String nmRoute) {
        this.nmRoute = nmRoute;
    }

    public String getNmRoute() {
        return this.nmRoute;
    }

    public void setNmRoute(String nmRoute) {
        this.nmRoute = nmRoute;
    }

    public int getIdRoute() {
        return this.idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public UUID getUid() {
        return this.uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
