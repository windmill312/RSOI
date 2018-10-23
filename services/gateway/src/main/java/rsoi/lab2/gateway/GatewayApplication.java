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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.model.FlightInfo;
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
                .route("pingFlight", p -> p
                        .path("/flights/ping")
                        .uri("http://localhost:8083/flights/ping")
                )
                .route("addFlight", p -> p
                        .path("/flights/add")
                        .filters(f -> f.rewritePath("/flights/add(?<idRoute>,<dtFlight>,<maxTickets>)", "/flights/add?${idRoute},{dtFlight},{maxTickets}"))
                        .uri("http://localhost:8083/flights/add")
                )
                .route("editFlight", p -> p
                        .path("/flights/edit")
                        .filters(f -> f.rewritePath("/flights/edit(?<idFlight>,<nnTickets>)", "/flights/edit?${idFlight},{nnTickets}"))
                        .uri("http://localhost:8083/flights/edit")
                )
                .route("showFlight", p -> p
                        .path("/flights/show")
                        .filters(f -> f.rewritePath("/flights/show?<idFlight>", "/flights/show?${idFlight}"))
                        .uri("http://localhost:8083/flights/show")
                )
                .route("flights", p -> p
                        .path("/flights")
                        .uri("http://localhost:8083/flights/")
                )
                .route("deleteFlight", p -> p
                        .path("/flights/delete")
                        .filters(f -> f.rewritePath("/flights/delete?<idFlight>", "/flights/delete?${idFlight}"))
                        .uri("http://localhost:8083/flights/delete")
                )
                //Routes Service
                .route("pingRoutes", p -> p
                        .path("/routes/ping")
                        .uri("http://localhost:8082/routes/ping")
                )
                .route("addRoute", p -> p
                        .path("/routes/add")
                        .filters(f -> f.rewritePath("/routes/add?<routeName>", "/flights/add?${routeName}"))
                        .uri("http://localhost:8082/routes/add")
                )
                .route("editRoute", p -> p
                        .path("/routes/edit")
                        .filters(f -> f.rewritePath("/routes/edit(?<idRoute>,<nmRoute>)", "/routes/edit?${idRoute},{nmRoute}"))
                        .uri("http://localhost:8082/routes/edit")
                )
                .route("showRoute", p -> p
                        .path("/routes/show")
                        .filters(f -> f.rewritePath("/routes/show?<idRoute>", "/routes/show?${idRoute}"))
                        .uri("http://localhost:8082/routes/show")
                )
                .route("routes", p -> p
                        .path("/routes")
                        .uri("http://localhost:8082/routes/")
                )
                //Tickets Service
                .route("pingTickets", p -> p
                        .path("/tickets/ping")
                        .uri("http://localhost:8081/tickets/ping")
                )
                .route("editTicket", p -> p
                        .path("/tickets/edit")
                        .filters(f -> f.rewritePath("/tickets/edit(?<idTicket>,<classType>)", "/tickets/edit?${idTicket},{classType}"))
                        .uri("http://localhost:8081/tickets/edit")
                )
                .route("showTicket", p -> p
                        .path("/tickets/show")
                        .filters(f -> f.rewritePath("/tickets/show?<idTicket>", "/tickets/show?${idTicket}"))
                        .uri("http://localhost:8081/tickets/show")
                )
                .route("tickets", p -> p
                        .path("/tickets")
                        .uri("http://localhost:8081/tickets/")
                )
                .route("deleteFlightTickets", p -> p
                        .path("/tickets/deleteFlightTickets")
                        .filters(f -> f.rewritePath("/tickets/deleteFlightTickets?<idFlight>", "/tickets/deleteFlightTickets?${idFlight}"))
                        .uri("http://localhost:8081/tickets/deleteFlightTickets")
                )
                .build();
    }

    @GetMapping(value = "/findFlightsAndTicketsByRoute",
            params = "idRoute",
            produces = "application/json")
    public String findFlightsAndTickets(@RequestParam int idRoute) throws IOException, JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights/routeFlights?idRoute=" + idRoute;
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

            resourceUrl = "http://localhost:8081/tickets/flightTickets?idFlight=" + object.getString("idFlight");
            ResponseEntity<?> responseTickets = restTemplate.getForEntity(resourceUrl, Object.class);
            String jsonTickets = mapper.writeValueAsString(responseTickets.getBody());
            JSONArray jsonTicketsArray = new JSONArray(jsonTickets);
            List<TicketInfo> listTicketInfo = new ArrayList<>();
            for (int j = 0; j < jsonTicketsArray.length(); j++) {
                listTicketInfo.add(gson.fromJson(jsonTicketsArray.getJSONObject(j).toString(), TicketInfo.class));
            }

            flightInfo.setListTickets(listTicketInfo);
            listFlightInfo.add(flightInfo);
        }

        return gson.toJson(listFlightInfo);
    }

    @GetMapping(value = "/tickets/add",
            params = {"classType", "idFlight", "idPassenger"})
    public String addTickets(@RequestParam String classType, @RequestParam int idFlight, @RequestParam int idPassenger) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String resourceUrl = "http://localhost:8083/flights/show?idFlight=" + idFlight;
        ResponseEntity<?> responseNnTickets = restTemplate.getForEntity(resourceUrl, Object.class);
        if (responseNnTickets.getStatusCode() == HttpStatus.OK) {
            String flightObject = mapper.writeValueAsString(responseNnTickets.getBody());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FlightInfo flightJson = gson.fromJson(flightObject, FlightInfo.class);
            int nnMaxTickets = flightJson.getMaxTickets();
            logger.info("Method 'addTickets': nnMaxTickets=" + nnMaxTickets);

            resourceUrl = "http://localhost:8081/tickets/add?classType=" + classType + "&idFlight=" + idFlight + "&idPassenger=" + idPassenger + "&nnMaxTickets=" + nnMaxTickets;
            ResponseEntity<?> responseTicket = restTemplate.getForEntity(resourceUrl, Object.class);

            int nnNewTicket = Integer.valueOf(responseTicket.getBody().toString()).intValue();
            logger.info("Method 'addTickets': nnNewTicket=" + nnNewTicket);

            if (nnNewTicket != -1) {
                resourceUrl = "http://localhost:8083/flights/edit?idFlight=" + idFlight + "&nnTickets=" + (nnNewTicket + 1);
                ResponseEntity<?> responseFlight = restTemplate.getForEntity(resourceUrl, Object.class);
                String flightRequest = mapper.writeValueAsString(responseFlight.getBody());
                if (flightRequest.equals("true"))
                    return "Done";
                else
                    return "Server error";
            } else
                return "There is no more tickets for this flight!";
        } else
            return "Server error";
    }

    @GetMapping(value = "/tickets/delete",
            params = {"idTicket"})
    public String deleteTicket(@RequestParam int idTicket) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String resourceUrl = "http://localhost:8081/tickets/show?idTicket=" + idTicket;
        ResponseEntity<?> responseTicket = restTemplate.getForEntity(resourceUrl, Object.class);
        String ticketObject = mapper.writeValueAsString(responseTicket.getBody());
        if (ticketObject == "-1")
            return "There is no tickets";
        else {
            TicketInfo ticketInfo = new GsonBuilder().setPrettyPrinting().create().fromJson(ticketObject, TicketInfo.class);

            resourceUrl = "http://localhost:8081/tickets/countTickets?idFlight=" + ticketInfo.getIdFlight();
            ResponseEntity<?> nnTicketsResponse = restTemplate.getForEntity(resourceUrl, Object.class);
            String nnTickets = mapper.writeValueAsString(nnTicketsResponse.getBody());

            resourceUrl = "http://localhost:8081/tickets/delete?idTicket=" + idTicket;
            ResponseEntity<?> deleteResponse = restTemplate.getForEntity(resourceUrl, Object.class);
            if (deleteResponse.getStatusCode() == HttpStatus.OK) {
                resourceUrl = "http://localhost:8083/flights/edit?idFlight=" + ticketInfo.getIdFlight() + "&nnTickets=" + (Integer.valueOf(nnTickets).intValue() - 1);
                ResponseEntity<?> responseFlight = restTemplate.getForEntity(resourceUrl, Object.class);
                String flightRequest = responseFlight.getBody().toString();

                if (flightRequest.equals("true"))
                    return "Done";
                else
                    return "Server error";
            } else
                return "Server error!";
        }
    }

    @GetMapping(value = "/flights/delete",
            params = {"idFlight"})
    public String deleteFlight(@RequestParam int idFlight) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String resourceUrl = "http://localhost:8081/tickets/deleteFlightTickets?idFlight=" + idFlight;
        ResponseEntity<?> responseTicketsDelete = restTemplate.getForEntity(resourceUrl, Object.class);
        String ticketObject = responseTicketsDelete.getBody().toString();
        if (ticketObject == "true") {
            resourceUrl = "http://localhost:8083/flights/delete?idFlight=" + idFlight;
            ResponseEntity<?> responseFlightDelete = restTemplate.getForEntity(resourceUrl, Object.class);
            String flightObject = responseFlightDelete.getBody().toString();
            if (flightObject == "true")
                return "Done";
            else
                return "Service error!";
        } else
            return "Service error!";
    }

    @GetMapping(value = "/routes/delete",
            params = {"idRoute"})
    public String deleteRoute(@RequestParam int idRoute) throws JSONException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights/routeFlights?idRoute=" + idRoute;
        ResponseEntity<?> responseFlight = restTemplate.getForEntity(resourceUrl, Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String flight = mapper.writeValueAsString(responseFlight.getBody());

        JSONArray jsonFlightArray = new JSONArray(flight);

        for (int i = 0; i < jsonFlightArray.length(); i++) {
            JSONObject flightObject = jsonFlightArray.getJSONObject(i);
            resourceUrl = "http://localhost:8081/tickets/deleteFlightTickets?idFlight=" + flightObject.get("idFlight");
            ResponseEntity<?> responseTicketsDelete = restTemplate.getForEntity(resourceUrl, Object.class);
            if (responseTicketsDelete.getStatusCode() == HttpStatus.OK) {
                resourceUrl = "http://localhost:8083/flights/delete?idFlight=" + flightObject.get("idFlight");
                ResponseEntity<?> responseFlightDelete = restTemplate.getForEntity(resourceUrl, Object.class);
                if (responseFlightDelete.getStatusCode() != HttpStatus.OK)
                    return "Service error! (Flight service down)";
            } else
                return "Service error! (Ticket service down)";

        }

        resourceUrl = "http://localhost:8082/routes/delete?idRoute=" + idRoute;
        ResponseEntity<?> responseRouteDelete = restTemplate.getForEntity(resourceUrl, Object.class);
        if (responseRouteDelete.getStatusCode() == HttpStatus.OK)
            return "Done";
        else
            return "Service error! (Route service down)";

    }
}
