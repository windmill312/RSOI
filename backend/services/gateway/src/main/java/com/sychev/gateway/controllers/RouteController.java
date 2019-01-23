package com.sychev.gateway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sychev.gateway.common.CheckToken;
import com.sychev.gateway.exception.InvalidTokenException;
import com.sychev.gateway.model.FlightInfo;
import com.sychev.gateway.model.RouteInfo;
import com.sychev.gateway.model.TicketInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import com.sychev.gateway.redis.MessagePublisher;
import com.sychev.gateway.redis.model.Task;
import com.sychev.gateway.redis.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController
public class RouteController {

    private Logger logger = Logger.getLogger(RouteController.class.getName());
    private final MessagePublisher messagePublisher;

    @Autowired
    public RouteController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @Value("${app.gatewayUuid}")
    private String gatewayUuid;

    @Value("${app.services.route}")
    private String routeServiceUrl;

    @Value("${app.services.flight}")
    private String flightServiceUrl;

    @Value("${app.services.ticket}")
    private String ticketServiceUrl;

    @GetMapping(value = "/routes")
    public ResponseEntity<?> getRoutes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getRoutes)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = routeServiceUrl + "/routes?page=" + page + "&size=" + size + "&gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/pingRoutes")
    public ResponseEntity<?> pingRoutes() {
        logger.info("Get request (pingRoutes)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = routeServiceUrl + "/ping?gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/countRoutes")
    public ResponseEntity<?> countRoutes() {
        logger.info("Get request (countRoutes)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = routeServiceUrl + "/countAll?gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, String.class);
    }

    @GetMapping(value = "/route",
            params = {"uidRoute"})
    public ResponseEntity<?> getRoute(
            @RequestParam String uidRoute) {
        logger.info("Get request (getRoute)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = routeServiceUrl + "/route?uidRoute=" + uidRoute + "&gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/route",
            params = "nmRoute")
    public ResponseEntity<?> getRouteByNm(
            @RequestParam String nmRoute) {
        logger.info("Get request (getRouteByNm)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = routeServiceUrl + "/routes?nmRoute=" + nmRoute + "&gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @PutMapping(value = "/route")
    public ResponseEntity addRoute(@RequestHeader(name = "Authorization") String accessToken,
                                   @RequestHeader(name = "User") String userUuid,
                                   @RequestHeader(name = "Service") String serviceUuid,
                                   @RequestBody RouteInfo routeInfo) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                logger.info("Get PUT request (addRoute)");
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String resourceUrl = routeServiceUrl + "/route?gatewayUuid=" + gatewayUuid;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                HttpEntity<RouteInfo> request = new HttpEntity<>(routeInfo, headers);
                ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.PUT, request, String.class);
                if (response.getStatusCode().equals(HttpStatus.OK))
                    return response;
                else {
                    logger.info("Server error while creating new route");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while creating new route");
                }
            } catch (Exception ex) {
                logger.info(ex.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
            }
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @PatchMapping(value = "/route")
    public ResponseEntity editRoute(@RequestHeader(name = "Authorization") String accessToken,
                                    @RequestHeader(name = "User") String userUuid,
                                    @RequestHeader(name = "Service") String serviceUuid,
                                    @RequestBody RouteInfo routeInfo) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                logger.info("Get PATCH request (editRoute)");
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String resourceUrl = routeServiceUrl + "/route?_method=patch" + "&gatewayUuid=" + gatewayUuid;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                HttpEntity<RouteInfo> request = new HttpEntity<>(routeInfo, headers);
                return restTemplate.postForObject(resourceUrl, request, ResponseEntity.class);
            } catch (Exception ex) {
                logger.info(ex.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
            }
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/flightsAndTicketsByRoute",
            params = "uidRoute",
            produces = "application/json")
    public ResponseEntity findFlightsAndTickets(@RequestHeader(name = "Authorization") String accessToken,
                                                @RequestHeader(name = "User") String userUuid,
                                                @RequestHeader(name = "Service") String serviceUuid,
                                                @RequestParam String uidRoute,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "5") int size) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String resourceUrl = flightServiceUrl + "/flights?uidRoute=" + uidRoute + "&gatewayUuid=" + gatewayUuid;
                ResponseEntity responseFlight = restTemplate.getForEntity(resourceUrl, String.class);

                String flight = responseFlight.getBody().toString();

                JSONArray jsonFlightArray = new JSONArray(flight);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                List<FlightInfo> listFlightInfo = new ArrayList<>();

                boolean isTicketsAvailable = false;
                try {
                    RestTemplate restTickets = new RestTemplate();
                    String ticketsUrl = ticketServiceUrl + "/ping?gatewayUuid=" + gatewayUuid;
                    ResponseEntity responseEntity = restTickets.getForEntity(ticketsUrl, Object.class);
                    if (responseEntity.getStatusCode() == HttpStatus.OK)
                        isTicketsAvailable = true;
                } catch (ResourceAccessException ex) {
                    logger.info("Ticket service is not available");
                    isTicketsAvailable = false;
                }

                for (int i = 0; i < jsonFlightArray.length(); i++) {
                    JSONObject object = jsonFlightArray.getJSONObject(i);
                    FlightInfo flightInfo = gson.fromJson(object.toString(), FlightInfo.class);

                    if (isTicketsAvailable) {
                        resourceUrl = ticketServiceUrl + "/flightTickets?uidFlight=" + object.getString("uid") + "&gatewayUuid=" + gatewayUuid;
                        ResponseEntity responseTickets = restTemplate.getForEntity(resourceUrl, String.class);
                        String jsonTickets = responseTickets.getBody().toString();
                        JSONArray jsonTicketsArray = new JSONArray(jsonTickets);
                        List<TicketInfo> listTicketInfo = new ArrayList<>();
                        for (int j = 0; j < jsonTicketsArray.length(); j++) {
                            listTicketInfo.add(gson.fromJson(jsonTicketsArray.getJSONObject(j).toString(), TicketInfo.class));
                        }

                        flightInfo.setTickets(listTicketInfo);
                    }
                    listFlightInfo.add(flightInfo);
                }

                if ((size * page) > listFlightInfo.size())
                    listFlightInfo = listFlightInfo.subList((size * (page - 1)), (size * page) - ((size * page) - listFlightInfo.size()));
                else
                    listFlightInfo = listFlightInfo.subList(size * (page - 1), size * page);

                return ResponseEntity.ok(gson.toJson(listFlightInfo));
            } catch (Exception ex) {
                logger.info(ex.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error because of exception (" + ex.getLocalizedMessage() + ")");
            }
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @DeleteMapping(value = "/route")
    public ResponseEntity deleteRoute(@RequestHeader(name = "Authorization") String accessToken,
                                      @RequestHeader(name = "User") String userUuid,
                                      @RequestHeader(name = "Service") String serviceUuid,
                                      @RequestBody String uidRoute) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String resourceUrl = flightServiceUrl + "/flights?uidRoute=" + uidRoute + "&gatewayUuid=" + gatewayUuid;
                ResponseEntity responseFlights = restTemplate.getForEntity(resourceUrl, String.class);

                JSONArray jsonFlightArray = new JSONArray(responseFlights.getBody().toString());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

                for (int i = 0; i < jsonFlightArray.length(); i++) {
                    //removing flight tickets
                    JSONObject flightObject = jsonFlightArray.getJSONObject(i);
                    resourceUrl = ticketServiceUrl + "/tickets?gatewayUuid=" + gatewayUuid;
                    HttpEntity<Object> requestTickets = new HttpEntity<>(flightObject.get("uid"), headers);
                    ResponseEntity responseTickets = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestTickets, Object.class);
                    //removing flight
                    if (responseTickets.getStatusCode().equals(HttpStatus.OK)) {
                        logger.info("Tickets of flight " + flightObject.get("uid") + " successfully removed");
                        resourceUrl = flightServiceUrl + "/flight?gatewayUuid=" + gatewayUuid;
                        HttpEntity<Object> requestFlight = new HttpEntity<>(flightObject.get("uid"), headers);
                        ResponseEntity responseFlight = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestFlight, Object.class);
                        if (!responseFlight.getStatusCode().equals(HttpStatus.OK))
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while removing flight");
                    } else
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while removing tickets");
                }
                //removing route
                logger.info("Flights successfully removed");
                resourceUrl = routeServiceUrl + "/route?gatewayUuid=" + gatewayUuid;
                HttpEntity<String> requestRoute = new HttpEntity<>(uidRoute, headers);
                ResponseEntity responseRoute = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestRoute, Object.class);
                if (responseRoute.getStatusCode() == HttpStatus.OK)
                    return responseRoute;
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while removing route");
            } catch (ResourceAccessException e) {
                logger.info("Transaction rollback! Service not available:" + e.getLocalizedMessage());
                transactionRollback();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error because of exception (" + e.getLocalizedMessage() + ")");
            } catch (Exception ex) {
                logger.info(ex.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error because of exception (" + ex.getLocalizedMessage() + ")");
            }
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    private ResponseEntity<String> transactionRollback() {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = flightServiceUrl + "/rollback?gatewayUuid=" + gatewayUuid;

        try {
            restTemplate.postForEntity(resourceUrl, null, String.class);
        } catch (ResourceAccessException ex) {
            logger.info(ex.getLocalizedMessage());
            messagePublisher.publish(
                    Topic.TASK.getName(),
                    new Task(resourceUrl, null));
        }

        try {
            resourceUrl = ticketServiceUrl + "/rollback?gatewayUuid=" + gatewayUuid;
            restTemplate.postForEntity(resourceUrl, null, String.class);
        } catch (ResourceAccessException ex) {
            logger.info(ex.getLocalizedMessage());
            messagePublisher.publish(
                    Topic.TASK.getName(),
                    new Task(resourceUrl, null));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Transaction rollbacked!");
    }
}
