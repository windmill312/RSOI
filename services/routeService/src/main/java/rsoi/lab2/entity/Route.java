package rsoi.lab2.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_route;
    @Column(name = "nm_route")
    private String nmRoute;
    @Column
    private UUID uid;

    public Route() {

    }

    public Route(String nmRoute) {
        this.nmRoute = nmRoute;
    }

    public String getNmRoute() {
        return nmRoute;
    }

    public void setNmRoute(String nmRoute) {
        this.nmRoute = nmRoute;
    }

    public int getIdRoute() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
