package rsoi.lab2.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rsoi.lab2.entity.Route;
import rsoi.lab2.model.RouteInfo;

import java.util.List;

public interface RouteService {

    @NonNull
    List<RouteInfo> listAll();

    @Nullable
    Route getRouteById(int id);

    @Nullable
    RouteInfo getRouteInfoById(int id);

    @Nullable
    List<RouteInfo> listAllByNmRoute(String nmRoute);

    Route saveOrUpdate(Route ticket);

    void delete(int id);

}
