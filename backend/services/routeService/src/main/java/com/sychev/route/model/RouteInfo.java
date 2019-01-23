package com.sychev.route.model;

import java.util.UUID;

public class RouteInfo {

    private int idRoute;

    private String nmRoute;

    private UUID uid;

    public int getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public String getRouteName() {
        return nmRoute;
    }

    public void setRouteName(String nmRoute) {
        this.nmRoute = nmRoute;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
