package rsoi.lab2.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.model.FlightInfo;
import rsoi.lab2.gateway.model.RouteInfo;
import rsoi.lab2.gateway.model.TicketInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@SpringBootApplication
public class GatewayApplication {
    Logger logger = Logger.getLogger(GatewayApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                //Flights Service
                .route("pingFlights", p -> p
                        .path("/pingFlights")
                        .uri("http://localhost:8083/ping")
                )
                .route("showFlight", p -> p
                        .path("/flight")
                        .filters(f -> f.rewritePath("/flight?<idFlight>", "/flight?${idFlight}"))
                        .uri("http://localhost:8083/flight")
                )
                .route("flights", p -> p
                        .path("/flights")
                        .uri("http://localhost:8083/flights")
                )
                //Routes Service
                .route("pingRoutes", p -> p
                        .path("/pingRoutes")
                        .uri("http://localhost:8082/ping")
                )
                .route("showRoute", p -> p
                        .path("/route")
                        .filters(f -> f.rewritePath("/route?<idRoute>", "/route?${idRoute}"))
                        .uri("http://localhost:8082/route")
                )
                .route("routes", p -> p
                        .path("/routes")
                        .uri("http://localhost:8082/routes")
                )
                //Tickets Service
                .route("pingTickets", p -> p
                        .path("/pingTickets")
                        .uri("http://localhost:8081/ping")
                )
                .route("showTicket", p -> p
                        .path("/ticket")
                        .filters(f -> f.rewritePath("/ticket?<idTicket>", "/ticket?${idTicket}"))
                        .uri("http://localhost:8081/ticket")
                )
                .route("tickets", p -> p
                        .path("/tickets")
                        .uri("http://localhost:8081/tickets")
                )
                .build();
    }

    @PutMapping(value = "/ticket")
    public String addTicket(@RequestBody TicketInfo ticketInfo) throws JsonProcessingException {
        try {
            logger.info("Get PUT request (addTicket)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8083/flight?idFlight=" + ticketInfo.getIdFlight();
            ResponseEntity<?> responseNnTickets = restTemplate.getForEntity(resourceUrl, Object.class);
            if (responseNnTickets.getStatusCode() == HttpStatus.OK) {
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
                    ResponseEntity<Integer> responseTicket = restTemplate.exchange("http://localhost:8081/ticket", HttpMethod.PUT, requestTicket, Integer.class);
                    if (responseTicket.getStatusCode() == HttpStatus.OK && responseTicket.getBody() != -1) {
                        logger.info("Ticket created with id " + responseTicket.getBody());
                        flightInfo.setIdFlight(responseTicket.getBody());
                        flightInfo.setNnTickets(flightInfo.getNnTickets() + 1);
                        HttpEntity<FlightInfo> requestFlight = new HttpEntity<>(flightInfo, headers);
                        ResponseEntity<String> responseFlight = restTemplate.exchange("http://localhost:8081/ticket", HttpMethod.PATCH, requestFlight, String.class);
                        if (responseFlight.getStatusCode() == HttpStatus.OK && responseFlight.getBody() == "Done")
                            return "Ticket created with id " + responseTicket.getBody();
                        else
                            return "Server error while incrementing nnTickets";
                    } else
                        return "Server error while creating new ticket";
                } else
                    return "There is no more tickets for this flight!";
            } else
                return "Server error while getting flight nnTickets";
        } catch (JsonProcessingException ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @PutMapping(value = "/flight")
    public String addFlight(@RequestBody FlightInfo flightInfo) throws Exception {
        try {
            logger.info("Get PUT request (addFlight)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8083/flight";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<FlightInfo> request = new HttpEntity<>(flightInfo, headers);
            ResponseEntity<Integer> response = restTemplate.exchange(resourceUrl, HttpMethod.PUT, request, Integer.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != -1)
                return "Flight created with id " + response.getBody();
            else {
                logger.info("Server error while creating new flight");
                return "Server error while creating new flight";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @PutMapping(value = "/route")
    public String addRoute(@RequestBody RouteInfo routeInfo) throws Exception {
        try {
            logger.info("Get PUT request (addRoute)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8082/route";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<RouteInfo> request = new HttpEntity<>(routeInfo, headers);
            ResponseEntity<Integer> response = restTemplate.exchange(resourceUrl, HttpMethod.PUT, request, Integer.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != -1)
                return "Route created with id " + response.getBody();
            else {
                logger.info("Server error while creating new route");
                return "Server error while creating new route";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @PatchMapping(value = "/route")
    public String editRoute(@RequestBody RouteInfo routeInfo) throws Exception {
        try {
            logger.info("Get PATCH request (editRoute)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8082/route";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<RouteInfo> request = new HttpEntity<>(routeInfo, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.PATCH, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() == "Done")
                return "Route updated";
            else {
                logger.info("Server error while updating route");
                return "Server error while updating route";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @PatchMapping(value = "/flight")
    public String editFlight(@RequestBody FlightInfo flightInfo) throws Exception {
        try {
            logger.info("Get PATCH request (editFlight)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8083/flight";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<FlightInfo> request = new HttpEntity<>(flightInfo, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.PATCH, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() == "Done")
                return "Flight updated";
            else {
                logger.info("Server error while updating flight");
                return "Server error while updating flight";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @PatchMapping(value = "/ticket")
    public String editTicket(@RequestBody TicketInfo ticketInfo) throws Exception {
        try {
            logger.info("Get PATCH request (editTicket)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/ticket";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<TicketInfo> request = new HttpEntity<>(ticketInfo, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.PATCH, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() == "Done")
                return "Ticket updated";
            else {
                logger.info("Server error while updating ticket");
                return "Server error while updating ticket";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @DeleteMapping(value = "/ticket")
    public String deleteTicket(@RequestBody int idTicket) throws Exception {
        try {
            logger.info("Get DELETE request (deleteTicket)");
            //get ticket
            //save idFlight
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/ticket";
            TicketInfo ticketInfo = new Gson().fromJson(mapper.writeValueAsString(restTemplate.getForEntity(resourceUrl, Object.class).getBody()), TicketInfo.class);
            int idFlight = ticketInfo.getIdFlight();
            //remove ticket
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<Integer> request = new HttpEntity<>(idTicket, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() == "Done") {
                //update flight
                resourceUrl = "http://localhost:8083/flight?idFlight=" + idFlight;
                ResponseEntity<?> responseFlight = restTemplate.getForEntity(resourceUrl, Object.class);
                FlightInfo flightInfo = new Gson().fromJson(mapper.writeValueAsString(responseFlight.getBody()), FlightInfo.class);
                flightInfo.setNnTickets(flightInfo.getNnTickets() - 1);
                editFlight(flightInfo);
                return "Ticket removed";
            } else {
                logger.info("Server error while removing ticket");
                return "Server error while removing ticket";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @DeleteMapping(value = "/tickets")
    public String deleteTickets(@RequestBody int idFlight) throws Exception {
        try {
            logger.info("Get DELETE request (deleteTickets)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/tickets";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<Integer> request = new HttpEntity<>(idFlight, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() == "Done")
                return "Tickets of flight" + idFlight + " removed";
            else {
                logger.info("Server error while removing tickets");
                return "Server error while removing tickets";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @DeleteMapping(value = "/route")
    public String deleteRoute(@RequestBody RouteInfo routeInfo) throws Exception {
        try {
            logger.info("Get DELETE request (deleteRoute)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8082/route";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<RouteInfo> request = new HttpEntity<>(routeInfo, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() == "Done")
                return "Route removed";
            else {
                logger.info("Server error while removing route");
                return "Server error while removing route";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }


    @GetMapping(value = "/findFlightsAndTicketsByRoute",
            params = "idRoute",
            produces = "application/json")
    public String findFlightsAndTickets(@RequestParam int idRoute) throws IOException, JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights?idRoute=" + idRoute;
        ResponseEntity<?> responseFlight = restTemplate.getForEntity(resourceUrl, Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String flight = mapper.writeValueAsString(responseFlight.getBody());

        JSONArray jsonFlightArray = new JSONArray(flight);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<FlightInfo> listFlightInfo = new ArrayList<>();

        for (int i = 0; i < jsonFlightArray.length(); i++) {
            JSONObject object = jsonFlightArray.getJSONObject(i);
            FlightInfo flightInfo = gson.fromJson(object.toString(), FlightInfo.class);

            resourceUrl = "http://localhost:8081/tickets?idFlight=" + object.getString("idFlight");
            ResponseEntity<?> responseTickets = restTemplate.getForEntity(resourceUrl, Object.class);
            String jsonTickets = mapper.writeValueAsString(responseTickets.getBody());
            JSONArray jsonTicketsArray = new JSONArray(jsonTickets);
            List<TicketInfo> listTicketInfo = new ArrayList<>();
            for (int j = 0; j < jsonTicketsArray.length(); j++) {
                listTicketInfo.add(gson.fromJson(jsonTicketsArray.getJSONObject(j).toString(), TicketInfo.class));
            }

            flightInfo.setTickets(listTicketInfo);
            listFlightInfo.add(flightInfo);
        }

        return gson.toJson(listFlightInfo);
    }

    @DeleteMapping("/flights")
    public String deleteFlight(@RequestParam int idFlight) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String resourceUrl = "http://localhost:8081/tickets";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Integer> request = new HttpEntity<>(idFlight, headers);
        ResponseEntity<String> responseTickets = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
        if (responseTickets.getStatusCode() == HttpStatus.OK) {
            resourceUrl = "http://localhost:8083/flight";
            HttpEntity<Integer> requestFlight = new HttpEntity<>(idFlight, headers);
            ResponseEntity<String> responseFlight = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestFlight, String.class);
            if (responseFlight.getStatusCode() == HttpStatus.OK && responseFlight.getBody() == "Done")
                return "Flight successfully removed";
            else
                return "Service error while flight removing";
        } else
            return "Service error while flight tickets removing";
    }


    @DeleteMapping("/route")
    public String deleteRoute(@RequestBody int idRoute) throws JSONException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights?idRoute=" + idRoute;
        ResponseEntity<?> responseFlights = restTemplate.getForEntity(resourceUrl, Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String flight = mapper.writeValueAsString(responseFlights.getBody());

        JSONArray jsonFlightArray = new JSONArray(flight);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        for (int i = 0; i < jsonFlightArray.length(); i++) {
            //removing flight tickets
            JSONObject flightObject = jsonFlightArray.getJSONObject(i);
            resourceUrl = "http://localhost:8081/tickets";
            HttpEntity<Object> requestTickets = new HttpEntity<>(flightObject.get("idFlight"), headers);
            ResponseEntity<String> responseTickets = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestTickets, String.class);
            //removing flight
            if (responseTickets.getStatusCode() == HttpStatus.OK && responseTickets.getBody() == "Done") {
                logger.info("Tickets of flight " + flightObject.get("idFlight") + " successfully removed");
                resourceUrl = "http://localhost:8083/flight";
                HttpEntity<Object> requestFlight = new HttpEntity<>(flightObject.get("idFlight"), headers);
                ResponseEntity<String> responseFlight = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestFlight, String.class);
                if (responseFlights.getStatusCode() != HttpStatus.OK || responseFlight.getBody() != "Done")
                    return "Service error while removing flight";
            } else
                return "Service error while removing tickets";
        }
        //removing route
        resourceUrl = "http://localhost:8082/route";
        HttpEntity<Object> requestRoute = new HttpEntity<>(idRoute, headers);
        ResponseEntity<?> responseRoute = restTemplate.getForEntity(resourceUrl, Object.class);
        if (responseRoute.getStatusCode() == HttpStatus.OK && responseRoute.getBody() == "Done")
            return "Route successfully removed";
        else
            return "Service error while removing route";
    }
}
