package com.sychev.route.services;

import com.sychev.route.model.RouteInfo;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import com.sychev.route.entity.Route;

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

    int countAll();
}
