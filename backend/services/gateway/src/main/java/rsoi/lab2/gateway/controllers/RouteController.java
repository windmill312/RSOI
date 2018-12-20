package rsoi.lab2.gateway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.common.CheckToken;
import rsoi.lab2.gateway.exception.InvalidTokenException;
import rsoi.lab2.gateway.model.FlightInfo;
import rsoi.lab2.gateway.model.RouteInfo;
import rsoi.lab2.gateway.model.TicketInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController
public class RouteController {

    private Logger logger = Logger.getLogger(RouteController.class.getName());

    @GetMapping(value = "/routes")
    public ResponseEntity<?> getRoutes(@RequestHeader(name = "Authentication") String accessToken,
                                       @RequestHeader(name = "User") String userUuid,
                                       @RequestHeader(name = "Service") String serviceUuid,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getRoutes)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8082/routes?page=" + page + "&size=" + size;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/pingRoutes")
    public ResponseEntity<?> pingRoutes(@RequestHeader(name = "Authentication") String accessToken,
                                        @RequestHeader(name = "User") String userUuid,
                                        @RequestHeader(name = "Service") String serviceUuid) {
        logger.info("Get request (pingTickets)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8082/ping";
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/countRoutes")
    public ResponseEntity<?> countRoutes(@RequestHeader(name = "Authentication") String accessToken,
                                         @RequestHeader(name = "User") String userUuid,
                                         @RequestHeader(name = "Service") String serviceUuid) {
        logger.info("Get request (countRoutes)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8082/countAll";
            return restTemplate.getForEntity(resourceUrl, String.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/route",
            params = {"uidRoute"})
    public ResponseEntity<?> getRoute(@RequestHeader(name = "Authentication") String accessToken,
                                      @RequestHeader(name = "User") String userUuid,
                                      @RequestHeader(name = "Service") String serviceUuid,
                                      @RequestParam String uidRoute) {
        logger.info("Get request (getRoute)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8082/route?uidRoute=" + uidRoute;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/route",
            params = "nmRoute")
    public ResponseEntity<?> getRouteByNm(@RequestHeader(name = "Authentication") String accessToken,
                                          @RequestHeader(name = "User") String userUuid,
                                          @RequestHeader(name = "Service") String serviceUuid,
                                          @RequestParam String nmRoute) {
        logger.info("Get request (getRouteByNm)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8082/routes?nmRoute=" + nmRoute;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @PutMapping(value = "/route")
    public ResponseEntity addRoute(@RequestHeader(name = "Authentication") String accessToken,
                                   @RequestHeader(name = "User") String userUuid,
                                   @RequestHeader(name = "Service") String serviceUuid,
                                   @RequestBody RouteInfo routeInfo) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            try {
                logger.info("Get PUT request (addRoute)");
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String resourceUrl = "http://localhost:8082/route";
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
    public ResponseEntity editRoute(@RequestHeader(name = "Authentication") String accessToken,
                                    @RequestHeader(name = "User") String userUuid,
                                    @RequestHeader(name = "Service") String serviceUuid,
                                    @RequestBody RouteInfo routeInfo) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            try {
                logger.info("Get PATCH request (editRoute)");
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String resourceUrl = "http://localhost:8082/route?_method=patch";
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
    public ResponseEntity findFlightsAndTickets(@RequestHeader(name = "Authentication") String accessToken,
                                                @RequestHeader(name = "User") String userUuid,
                                                @RequestHeader(name = "Service") String serviceUuid,
                                                @RequestParam String uidRoute,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "5") int size) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String resourceUrl = "http://localhost:8083/flights?uidRoute=" + uidRoute;
                ResponseEntity responseFlight = restTemplate.getForEntity(resourceUrl, String.class);

                String flight = responseFlight.getBody().toString();

                JSONArray jsonFlightArray = new JSONArray(flight);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                List<FlightInfo> listFlightInfo = new ArrayList<>();

                for (int i = 0; i < jsonFlightArray.length(); i++) {
                    JSONObject object = jsonFlightArray.getJSONObject(i);
                    FlightInfo flightInfo = gson.fromJson(object.toString(), FlightInfo.class);

                    resourceUrl = "http://localhost:8081/flightTickets?uidFlight=" + object.getString("uid");
                    ResponseEntity responseTickets = restTemplate.getForEntity(resourceUrl, String.class);
                    String jsonTickets = responseTickets.getBody().toString();
                    JSONArray jsonTicketsArray = new JSONArray(jsonTickets);
                    List<TicketInfo> listTicketInfo = new ArrayList<>();
                    for (int j = 0; j < jsonTicketsArray.length(); j++) {
                        listTicketInfo.add(gson.fromJson(jsonTicketsArray.getJSONObject(j).toString(), TicketInfo.class));
                    }

                    flightInfo.setTickets(listTicketInfo);
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
    public ResponseEntity deleteRoute(@RequestHeader(name = "Authentication") String accessToken,
                                      @RequestHeader(name = "User") String userUuid,
                                      @RequestHeader(name = "Service") String serviceUuid,
                                      @RequestBody String uidRoute) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String resourceUrl = "http://localhost:8083/flights?uidRoute=" + uidRoute;
                ResponseEntity responseFlights = restTemplate.getForEntity(resourceUrl, String.class);

                JSONArray jsonFlightArray = new JSONArray(responseFlights.getBody().toString());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

                for (int i = 0; i < jsonFlightArray.length(); i++) {
                    //removing flight tickets
                    JSONObject flightObject = jsonFlightArray.getJSONObject(i);
                    resourceUrl = "http://localhost:8081/tickets";
                    HttpEntity<Object> requestTickets = new HttpEntity<>(flightObject.get("uid"), headers);
                    ResponseEntity responseTickets = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestTickets, Object.class);
                    //removing flight
                    if (responseTickets.getStatusCode().equals(HttpStatus.OK)) {
                        logger.info("Tickets of flight " + flightObject.get("uid") + " successfully removed");
                        resourceUrl = "http://localhost:8083/flight";
                        HttpEntity<Object> requestFlight = new HttpEntity<>(flightObject.get("uid"), headers);
                        ResponseEntity responseFlight = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestFlight, Object.class);
                        if (! responseFlight.getStatusCode().equals(HttpStatus.OK))
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while removing flight");
                    } else
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while removing tickets");
                }
                //removing route
                logger.info("Flights successfully removed");
                resourceUrl = "http://localhost:8082/route";
                HttpEntity<String> requestRoute = new HttpEntity<>(uidRoute, headers);
                ResponseEntity responseRoute = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestRoute, Object.class);
                if (responseRoute.getStatusCode() == HttpStatus.OK)
                    return responseRoute;
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while removing route");
            } catch (Exception ex) {
                logger.info(ex.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error because of exception (" + ex.getLocalizedMessage() + ")");
            }
        } else
            throw new InvalidTokenException("Token is invalid");
    }
}
