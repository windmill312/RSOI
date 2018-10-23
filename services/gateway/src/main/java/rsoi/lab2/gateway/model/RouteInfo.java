package rsoi.lab2.gateway.model;


import java.util.UUID;

public class RouteInfo {

    private int id_route;

    private String nmRoute;

    private UUID uid;

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
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
