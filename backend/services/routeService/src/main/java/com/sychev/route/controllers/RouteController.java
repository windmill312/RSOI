package com.sychev.route.controllers;

import com.google.gson.Gson;
import com.sychev.route.entity.Route;
import com.sychev.route.model.RouteInfo;
import com.sychev.route.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class RouteController {

    @Autowired
    public RouteService routeService;
    private Logger logger = Logger.getLogger(RouteController.class.getName());

    @Value("${app.gatewayUuid}")
    private String gateway;

    public RouteController(RouteService service) {
        this.routeService = service;
    }

    @GetMapping(
            value = "/ping",
            params = {
                    "gatewayUuid"
            }
    )
    public ResponseEntity<Object> ping(@RequestParam String gatewayUuid) {
        logger.info("Get \"ping\" request.");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/countAll",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity countAll(@RequestParam String gatewayUuid) {
        logger.info("Get \"countRoutes\" request.");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(routeService.countAll());
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/routes",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity<List<RouteInfo>> getRoutes(@RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "5") int size,
                                                     @RequestParam String gatewayUuid) {
        logger.info("Get \"routes\" request with params (page=" + page + ", size=" + size + ").");
        if (gatewayUuid.equals(gateway)) {
            List<RouteInfo> list = routeService.listAll();
            if ((size * page) > list.size())
                return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
            else
                return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = "/route",
            params = {
                    "uidRoute",
                    "gatewayUuid"
            },
            produces = "application/json")
    public ResponseEntity<RouteInfo> getRoute(@RequestParam String uidRoute,
                                              @RequestParam String gatewayUuid) {
        logger.info("Get \"show\" request with param (uidRoute=" + uidRoute + ").");
        if (gatewayUuid.equals(gateway))
            return ResponseEntity.ok(routeService.getRouteInfoByUid(UUID.fromString(uidRoute)));
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = "/routes",
            params = {
                    "nmRoute",
                    "gatewayUuid"
            },
            produces = "application/json")
    public ResponseEntity<List<RouteInfo>> getRoutes(@RequestParam String nmRoute,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "5") int size,
                                                     @RequestParam String gatewayUuid) {
        logger.info("Get \"routes\" request with param (nmRoute=" + nmRoute + ", page=" + page + ", size=" + size + ").");
        if (gatewayUuid.equals(gateway)) {
            List<RouteInfo> list = routeService.listAllByNmRoute(nmRoute);
            if ((size * page) > list.size())
                return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
            else
                return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping(
            value = "/route",
            params = {
                    "gatewayUuid"
            }
    )
    public ResponseEntity<String> add(@RequestBody RouteInfo route,
                                      @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get PUT request (add) with params (routeName=" + route.getRouteName() + ")");
                Route newRoute = new Route(route.getRouteName());
                newRoute.setUid(UUID.randomUUID());
                routeService.saveOrUpdate(newRoute);
                return ResponseEntity.ok(new Gson().toJson(newRoute.getUid()));
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping(
            value = "/route",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity edit(@RequestBody Route route,
                               @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get PATCH request (edit) with param (uidRoute=" + route.getUid() + ", nmRoute=" + route.getNmRoute() + ").");
                Route newRoute = routeService.getRouteByUid(route.getUid());
                newRoute.setNmRoute(route.getNmRoute());
                routeService.saveOrUpdate(newRoute);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Transactional
    @DeleteMapping(
            value = "/route",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity delete(@RequestBody String uidRoute,
                                 @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            try {
                logger.info("Get DELETE request (delete) with param (uidRoute=" + uidRoute + ").");
                routeService.delete(UUID.fromString(uidRoute));
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
