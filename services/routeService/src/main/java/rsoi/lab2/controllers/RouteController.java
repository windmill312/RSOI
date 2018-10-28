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
public class RouteController {

    Logger logger = Logger.getLogger(RouteController.class.getName());

    @Autowired
    public RouteService routeService;

    public RouteController(RouteService service) {
        this.routeService = service;
    }

    @GetMapping("/ping")
    public PingResponse ping() {
        logger.info("Get \"ping\" request.");
        return new PingResponse("ok");
    }

    @GetMapping("/routes")
    public List<RouteInfo> getRoutes() {
        logger.info("Get \"routes\" request.");
        return routeService.listAll();
    }

    @GetMapping(value = "/route",
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

    @PutMapping("/route")
    public int add(@RequestBody RouteInfo route) {
        try {
            logger.info("Get PUT request (add) with param (routeName=" + route.getRouteName() + ").");
            Route newRoute = new Route(route.getRouteName());
            if (route.getIdRoute() != 0)
                newRoute.setIdRoute(route.getIdRoute());
            newRoute.setUid(UUID.randomUUID());
            routeService.saveOrUpdate(newRoute);
            return newRoute.getIdRoute();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return -1;
        }
    }

    @PatchMapping("/route")
    public String edit(@RequestBody Route route) {
        try {
            logger.info("Get PATCH request (edit) with param (idRoute=" + route.getIdRoute() + ", nmRoute=" + route.getNmRoute() + ").");
            Route newRoute = routeService.getRouteById(route.getIdRoute());
            newRoute.setNmRoute(route.getNmRoute());
            routeService.saveOrUpdate(newRoute);
            return "Done";
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return "Server error";
        }
    }

    @DeleteMapping("/route")
    public String delete(@RequestBody int idRoute) {
        try {
            logger.info("Get DELETE request (delete) with param (idRoute=" + idRoute + ").");
            routeService.delete(idRoute);
            return "Done";
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return "Server error";
        }
    }

}
