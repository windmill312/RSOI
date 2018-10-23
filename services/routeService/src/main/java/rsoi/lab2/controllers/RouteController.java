package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rsoi.lab2.entity.Route;
import rsoi.lab2.model.PingResponse;
import rsoi.lab2.model.RouteInfo;
import rsoi.lab2.services.RouteService;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/routes")
public class RouteController {

    Logger logger = Logger.getLogger(RouteController.class.getName());

    @Autowired
    private RouteService routeService;

    @GetMapping("/ping")
    public PingResponse ping() {
        logger.info("Get \"ping\" request.");
        return new PingResponse("ok");
    }

    @GetMapping
    public List<RouteInfo> listTickets() {
        logger.info("Get \"routes\" request.");
        return routeService.listAll();
    }

    @GetMapping(value = "/show",
            params = "idRoute",
            produces = "application/json")
    public RouteInfo getRoute(@RequestParam Integer idRoute) {
        logger.info("Get \"show\" request with param (idRoute=" + idRoute + ").");
        return routeService.getRouteInfoById(idRoute);
    }

    @GetMapping(value = "/routes",
            params = "nmRoute",
            produces = "application/json")
    public List<RouteInfo> getRoutes(@RequestParam String nmRoute) {
        logger.info("Get \"routes\" request with param (nmRoute=" + nmRoute + ").");
        return routeService.listAllByNmRoute(nmRoute);
    }

    @GetMapping(
            value = "/add",
            params = "routeName"
    )
    public String add(@RequestParam String routeName) {
        logger.info("Get \"add\" request with param (routeName=" + routeName + ").");
        Route route = new Route();
        route.setNmRoute(routeName);
        route.setUid(UUID.randomUUID());
        routeService.saveOrUpdate(route);
        return "Done";
    }

    @GetMapping(
            value = "/edit",
            params = {"idRoute", "nmRoute"}
    )
    public String edit(@RequestParam int idRoute,
                       @RequestParam String nmRoute) {
        logger.info("Get \"edit\" request with param (idRoute=" + idRoute + ", nmRoute=" + nmRoute + ").");
        Route route = routeService.getRouteById(idRoute);
        route.setNmRoute(nmRoute);
        routeService.saveOrUpdate(route);
        return "Done";
    }

    @GetMapping(value = "/delete",
            params = "idRoute")
    public boolean delete(@RequestParam String idRoute) {
        logger.info("Get \"delete\" request with param (idRoute=" + idRoute + ").");
        routeService.delete(Integer.valueOf(idRoute));
        return true;
    }

}
