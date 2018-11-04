package rsoi.lab2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rsoi.lab2.entity.Route;
import rsoi.lab2.model.RouteInfo;
import rsoi.lab2.repositories.RouteRepository;

import java.util.List;
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
    public Route getRouteById(int id) {
        return routeRepository.findById(id).orElse(null);
    }

    @Override
    public RouteInfo getRouteInfoById(int id) {
        return routeRepository.findById(id).map(this::buildRouteInfo).orElse(null);
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
    public void delete(int id) {
        routeRepository.deleteById(id);
    }

}
