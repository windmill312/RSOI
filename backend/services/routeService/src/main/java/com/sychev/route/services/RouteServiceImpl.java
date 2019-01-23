package com.sychev.route.services;

import com.sychev.route.model.RouteInfo;
import com.sychev.route.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sychev.route.entity.Route;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {

    private RouteRepository routeRepository;

    @Autowired
    public RouteServiceImpl(RouteRepository ticketRepository) {
        this.routeRepository = ticketRepository;
    }

    @Override
    public List<RouteInfo> listAll() {
        return routeRepository.findAll().stream().map(this::buildRouteInfo).collect(Collectors.toList());
    }

    private RouteInfo buildRouteInfo(Route route) {
        RouteInfo info = new RouteInfo();
        info.setIdRoute(route.getIdRoute());
        info.setRouteName(route.getNmRoute());
        info.setUid(route.getUid());
        return info;
    }

    @Override
    public Route getRouteByUid(UUID uidRoute) {
        return routeRepository.findByUid(uidRoute);
    }

    @Override
    public RouteInfo getRouteInfoByUid(UUID uidRoute) {
        return buildRouteInfo(routeRepository.findByUid(uidRoute));
    }

    @Override
    public List<RouteInfo> listAllByNmRoute(String nmRoute) {
        return routeRepository.findAllByNmRoute(nmRoute).stream().map(this::buildRouteInfo).collect(Collectors.toList());
    }

    @Override
    public Route saveOrUpdate(Route ticket) {
        routeRepository.save(ticket);
        return ticket;
    }

    @Override
    public void delete(UUID uidRoute) {
        routeRepository.deleteByUid(uidRoute);
    }

    @Override
    public int countAll() {
        return routeRepository.findAll().size();
    }

}
