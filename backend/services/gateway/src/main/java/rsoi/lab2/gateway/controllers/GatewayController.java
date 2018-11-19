package rsoi.lab2.gateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.model.FlightInfo;
import rsoi.lab2.gateway.model.RouteInfo;
import rsoi.lab2.gateway.model.TicketInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController
public class GatewayController {

    Logger logger = Logger.getLogger(GatewayController.class.getName());

    @GetMapping(value = "/flights")
    public ResponseEntity<?> getFlights(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights?page=" + page + "&size=" + size;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/flights",
            params = "uidRoute")
    public ResponseEntity<?> getFlightsByRoute(@RequestParam String uidRoute, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights?uidRoute=" + uidRoute + "&page=" + page + "&size=" + size;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/pingFlights")
    public ResponseEntity<?> pingFlights() {
        logger.info("Get request (pingFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/ping";
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/flight",
            params = {"uidFlight"})
    public ResponseEntity<?> getFlight(@RequestParam String uidFlight) {
        logger.info("Get request (getFlight)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flight?uidFlight=" + uidFlight;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/routes")
    public ResponseEntity<?> getRoutes(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getRoutes)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/routes?page=" + page + "&size=" + size;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/pingRoutes")
    public ResponseEntity<?> pingRoutes() {
        logger.info("Get request (pingTickets)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/ping";
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/route",
            params = {"uidRoute"})
    public ResponseEntity<?> getRoute(@RequestParam String uidRoute) {
        logger.info("Get request (getRoute)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/route?uidRoute=" + uidRoute;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/route",
            params = "nmRoute")
    public ResponseEntity<?> getRouteByNm(@RequestParam String nmRoute) {
        logger.info("Get request (getRouteByNm)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/routes?nmRoute=" + nmRoute;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/tickets")
    public ResponseEntity<?> getTickets(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getTickets)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/tickets?page=" + page + "&size=" + size;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/tickets",
            params = "uidFlight")
    public ResponseEntity<?> getTickets(@RequestParam String uidFlight, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getTicketsByFlight)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/flightTickets?uidFlight=" + uidFlight + "&page=" + page + "&size=" + size;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/pingTickets")
    public ResponseEntity<?> pingTickets() {
        logger.info("Get request (pingTickets)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/ping";
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/ticket",
            params = "uidTicket")
    public ResponseEntity<?> getTicket(@RequestParam String uidTicket) {
        logger.info("Get request (getTicket)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/ticket?uidTicket=" + uidTicket;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @PutMapping(value = "/ticket")
    public ResponseEntity addTicket(@RequestBody TicketInfo ticketInfo) {
        try {
            logger.info("Get PUT request (addTicket)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8083/flight?uidFlight=" + ticketInfo.getUidFlight();
            ResponseEntity<?> responseNnTickets = restTemplate.getForEntity(resourceUrl, Object.class);
            if (responseNnTickets.getStatusCode().equals(HttpStatus.OK)) {
                String flightObject = mapper.writeValueAsString(responseNnTickets.getBody());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                FlightInfo flightInfo = gson.fromJson(flightObject, FlightInfo.class);
                int nnTickets = flightInfo.getNnTickets();
                logger.info("nnTickets = " + nnTickets);
                int nnMaxTickets = flightInfo.getMaxTickets();
                logger.info("nnMaxTickets = " + nnMaxTickets);
                if (nnTickets < nnMaxTickets) {
                    logger.info("PUT add ticket");
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<TicketInfo> requestTicket = new HttpEntity<>(ticketInfo, headers);
                    ResponseEntity<String> responseTicket = restTemplate.exchange("http://localhost:8081/ticket", HttpMethod.PUT, requestTicket, String.class);
                    if (responseTicket.getStatusCode().equals(HttpStatus.OK)) {
                        logger.info("Ticket created with uid " + responseTicket.getBody());
                        flightInfo.setNnTickets(flightInfo.getNnTickets() + 1);
                        RestTemplate restTemplateFlight = new RestTemplate();

                        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                        requestFactory.setConnectTimeout(500);
                        requestFactory.setReadTimeout(500);
                        restTemplate.setRequestFactory(requestFactory);

                        HttpEntity<FlightInfo> requestFlight = new HttpEntity<>(flightInfo, headers);
                        restTemplateFlight.postForObject("http://localhost:8083/flight?_method=patch", requestFlight, ResponseEntity.class);
                        return responseTicket;
                    } else
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while creating new ticket");
                } else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is no more tickets for this flight!");
            } else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while getting flight nnTickets");
        } catch (JsonProcessingException ex) {
            logger.info(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
        }
    }

    @PutMapping(value = "/flight")
    public ResponseEntity addFlight(@RequestBody FlightInfo flightInfo) {
        try {
            logger.info("Get PUT request (addFlight)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8083/flight";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<FlightInfo> request = new HttpEntity<>(flightInfo, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.PUT, request, String.class);
            if (response.getStatusCode().equals(HttpStatus.OK))
                return response;
            else {
                logger.info("Server error while creating new flight");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while creating new flight");
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
        }
    }

    @PutMapping(value = "/route")
    public ResponseEntity addRoute(@RequestBody RouteInfo routeInfo) {
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
    }

    @PatchMapping(value = "/route")
    public ResponseEntity editRoute(@RequestBody RouteInfo routeInfo) {
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
    }

    @PatchMapping(value = "/flight")
    public ResponseEntity editFlight(@RequestBody FlightInfo flightInfo) {
        try {
            logger.info("Get PATCH request (editFlight)");
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8083/flight?_method=patch";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<FlightInfo> request = new HttpEntity<>(flightInfo, headers);
            return restTemplate.postForObject(resourceUrl, request, ResponseEntity.class);
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
        }
    }

    @PatchMapping(value = "/ticket")
    public ResponseEntity editTicket(@RequestBody TicketInfo ticketInfo) {
        try {
            logger.info("Get PATCH request (editTicket)");
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8081/ticket?_method=patch";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<TicketInfo> request = new HttpEntity<>(ticketInfo, headers);
            ResponseEntity response = restTemplate.postForObject(resourceUrl, request, ResponseEntity.class);
            return response;
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
        }
    }

    @DeleteMapping(value = "/ticket")
    public ResponseEntity deleteTicket(@RequestParam String uidTicket) {
        try {
            logger.info("Get DELETE request (deleteTicket)");
            //get ticket
            //save idFlight
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/ticket?uidTicket=" + uidTicket;
            ResponseEntity responseTicket = restTemplate.getForEntity(resourceUrl, String.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            TicketInfo ticketInfo = gson.fromJson(responseTicket.getBody().toString(), TicketInfo.class);
            String uidFlight = ticketInfo.getUidFlight().toString();
            //remove ticket
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> request = new HttpEntity<>(uidTicket, headers);
            ResponseEntity response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                //update flight
                resourceUrl = "http://localhost:8083/flight?uidFlight=" + uidFlight;
                ResponseEntity responseFlight = restTemplate.getForEntity(resourceUrl, Object.class);
                FlightInfo flightInfo = new Gson().fromJson(responseFlight.getBody().toString(), FlightInfo.class);
                flightInfo.setNnTickets(flightInfo.getNnTickets() - 1);
                editFlight(flightInfo);
                return responseFlight;
            } else {
                logger.info("Server error while removing ticket");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while removing ticket");
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
        }
    }

    @DeleteMapping(value = "/tickets")
    public ResponseEntity deleteTickets(@RequestParam String uidFlight) {
        try {
            logger.info("Get DELETE request (deleteTickets)");
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8081/tickets";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> request = new HttpEntity<>(uidFlight, headers);
            ResponseEntity response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode().equals(HttpStatus.OK))
                return response;
            else {
                logger.info("Server error while removing tickets");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while removing tickets");
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
        }
    }

    @GetMapping(value = "/flightsAndTicketsByRoute",
            params = "uidRoute",
            produces = "application/json")
    public ResponseEntity findFlightsAndTickets(@RequestParam String uidRoute, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
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
    }

    @DeleteMapping(value = "/flight")
    public ResponseEntity deleteFlight(@RequestBody String uidFlight) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String resourceUrl = "http://localhost:8081/tickets";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>(uidFlight, headers);
        ResponseEntity<String> responseTickets = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
        if (responseTickets.getStatusCode() == HttpStatus.OK) {
            resourceUrl = "http://localhost:8083/flight";
            HttpEntity<String> requestFlight = new HttpEntity<>(uidFlight, headers);
            ResponseEntity responseFlight = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestFlight, String.class);
            if (responseFlight.getStatusCode().equals(HttpStatus.OK))
                return responseFlight;
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while flight removing");
        } else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while flight tickets removing");
    }


    @DeleteMapping(value = "/route")
    public ResponseEntity deleteRoute(@RequestBody String uidRoute) {
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
    }
}
