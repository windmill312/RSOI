package rsoi.lab2.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rsoi.lab2.entity.Route;
import rsoi.lab2.model.RouteInfo;

import java.util.List;
import java.util.UUID;

public interface RouteService {

    @NonNull
    List<RouteInfo> listAll();

    @Nullable
    Route getRouteByUid(UUID uidRoute);

    @Nullable
    RouteInfo getRouteInfoByUid(UUID uidRoute);

    @Nullable
    List<RouteInfo> listAllByNmRoute(String nmRoute);

    Route saveOrUpdate(Route route);

    void delete(UUID uidRoute);

}
