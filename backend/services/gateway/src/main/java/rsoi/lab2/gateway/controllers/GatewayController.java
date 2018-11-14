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
    public String getFlights(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights?page=" + page + "&size=" + size;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/flights",
            params = "idRoute")
    public String getFlightsByRoute(@RequestParam int idRoute, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flights?idRoute=" + idRoute + "&page=" + page + "&size=" + size;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/pingFlights")
    public String pingFlights() {
        logger.info("Get request (pingFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/ping";
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/flight",
            params = {"idFlight"})
    public String getFlight(@RequestParam int idFlight) {
        logger.info("Get request (getFlight)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8083/flight?idFlight=" + idFlight;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/routes")
    public String getRoutes(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getRoutes)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/routes?page=" + page + "&size=" + size;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/pingRoutes")
    public String pingRoutes() {
        logger.info("Get request (pingTickets)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/ping";
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/route",
            params = {"idRoute"})
    public String getRoute(@RequestParam int idRoute) {
        logger.info("Get request (getRoute)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/route?idRoute=" + idRoute;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/route",
            params = "nmRoute")
    public String getRouteByNm(@RequestParam String nmRoute) {
        logger.info("Get request (getRouteByNm)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8082/routes?nmRoute=" + nmRoute;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/tickets")
    public String getTickets(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getTickets)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/tickets?page=" + page + "&size=" + size;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/tickets",
            params = "idFlight")
    public String getTickets(@RequestParam int idFlight, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getTicketsByFlight)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/tickets?idFlight=" + idFlight + "&page=" + page + "&size=" + size;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/pingTickets")
    public String pingTickets() {
        logger.info("Get request (pingTickets)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/ping";
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @GetMapping(value = "/ticket",
            params = {"idTicket"})
    public String getTicket(@RequestParam int idTicket) {
        logger.info("Get request (getTicket)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8081/ticket?idTicket=" + idTicket;
        ResponseEntity<?> response = restTemplate.getForEntity(resourceUrl, Object.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(response.getBody());
    }

    @PutMapping(value = "/ticket")
    public String addTicket(@RequestBody TicketInfo ticketInfo) {
        try {
            logger.info("Get PUT request (addTicket)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8083/flight?idFlight=" + ticketInfo.getIdFlight();
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
                    ResponseEntity<Integer> responseTicket = restTemplate.exchange("http://localhost:8081/ticket", HttpMethod.PUT, requestTicket, Integer.class);
                    if (responseTicket.getStatusCode().equals(HttpStatus.OK) && !responseTicket.getBody().equals(-1)) {
                        logger.info("Ticket created with id " + responseTicket.getBody());
                        flightInfo.setNnTickets(flightInfo.getNnTickets() + 1);
                        RestTemplate restTemplateFlight = new RestTemplate();

                        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                        requestFactory.setConnectTimeout(500);
                        requestFactory.setReadTimeout(500);
                        restTemplate.setRequestFactory(requestFactory);

                        HttpEntity<FlightInfo> requestFlight = new HttpEntity<>(flightInfo, headers);
                        String responseFlight = restTemplateFlight.postForObject("http://localhost:8083/flight?_method=patch", requestFlight, String.class);
                        if (responseFlight.equals("Done"))
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
    public String addFlight(@RequestBody FlightInfo flightInfo) {
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
            if (response.getStatusCode().equals(HttpStatus.OK) && !response.getBody().equals(-1))
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
    public String addRoute(@RequestBody RouteInfo routeInfo) {
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
            if (response.getStatusCode().equals(HttpStatus.OK) && !response.getBody().equals(-1))
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
    public String editRoute(@RequestBody RouteInfo routeInfo) {
        try {
            logger.info("Get PATCH request (editRoute)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8082/route?_method=patch";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<RouteInfo> request = new HttpEntity<>(routeInfo, headers);
            String response = restTemplate.postForObject(resourceUrl, request, String.class);
            if (response.equals("Done"))
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
    public String editFlight(@RequestBody FlightInfo flightInfo) {
        try {
            logger.info("Get PATCH request (editFlight)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8083/flight?_method=patch";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<FlightInfo> request = new HttpEntity<>(flightInfo, headers);
            String response = restTemplate.postForObject(resourceUrl, request, String.class);
            if (response.equals("Done"))
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
    public String editTicket(@RequestBody TicketInfo ticketInfo) {
        try {
            logger.info("Get PATCH request (editTicket)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/ticket?_method=patch";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<TicketInfo> request = new HttpEntity<>(ticketInfo, headers);
            String response = restTemplate.postForObject(resourceUrl, request, String.class);
            if (response.equals("Done"))
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
    public String deleteTicket(@RequestParam int idTicket) {
        try {
            logger.info("Get DELETE request (deleteTicket)");
            //get ticket
            //save idFlight
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/ticket?idTicket=" + idTicket;
            ResponseEntity<String> responseTicket = restTemplate.getForEntity(resourceUrl, String.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            TicketInfo ticketInfo = gson.fromJson(responseTicket.getBody(), TicketInfo.class);
            int idFlight = ticketInfo.getIdFlight();
            //remove ticket
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<Integer> request = new HttpEntity<>(idTicket, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody().equals("Done")) {
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
    public String deleteTickets(@RequestBody int idFlight) {
        try {
            logger.info("Get DELETE request (deleteTickets)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/ticket";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<Integer> request = new HttpEntity<>(idFlight, headers);
            ResponseEntity<String> response = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody().equals("Done"))
                return "Tickets of flight=" + idFlight + " removed";
            else {
                logger.info("Server error while removing tickets");
                return "Server error while removing tickets";
            }
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Server error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    /*@DeleteMapping(value = "/route")
    public String deleteRoute(@RequestBody RouteInfo routeInfo) {
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
    }*/


    @GetMapping(value = "/flightsAndTicketsByRoute",
            params = "idRoute",
            produces = "application/json")
    public String findFlightsAndTickets(@RequestParam int idRoute, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        try {
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

                resourceUrl = "http://localhost:8081/tickets?idFlight=" + object.getInt("idFlight");
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

            if ((size * page) > listFlightInfo.size())
                listFlightInfo = listFlightInfo.subList((size * (page - 1)), (size * page) - ((size * page) - listFlightInfo.size()));
            else
                listFlightInfo = listFlightInfo.subList(size * (page - 1), size * page);

            return gson.toJson(listFlightInfo);
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Service error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }

    @DeleteMapping(value = "/flight")
    public String deleteFlight(@RequestBody int idFlight) {
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
            if (responseFlight.getStatusCode() == HttpStatus.OK && responseFlight.getBody().equals("Done"))
                return "Flight successfully removed";
            else
                return "Service error while flight removing";
        } else
            return "Service error while flight tickets removing";
    }


    @DeleteMapping(value = "/route")
    public String deleteRoute(@RequestBody int idRoute) {
        try {
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
                    if (responseFlights.getStatusCode() != HttpStatus.OK || !responseFlight.getBody().equals("Done"))
                        return "Service error while removing flight";
                } else
                    return "Service error while removing tickets";
            }
            //removing route
            resourceUrl = "http://localhost:8082/route";
            HttpEntity<Object> requestRoute = new HttpEntity<>(idRoute, headers);
            ResponseEntity<String> responseRoute = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestRoute, String.class);
            if (responseRoute.getStatusCode() == HttpStatus.OK && responseRoute.getBody().equals("Done"))
                return "Route successfully removed";
            else
                return "Service error while removing route";
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return "Service error because of exception (" + ex.getLocalizedMessage() + ")";
        }
    }
}
