package rsoi.lab2.gateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.common.CheckToken;
//import rsoi.lab2.gateway.common.RequestQueue;
import rsoi.lab2.gateway.exception.InvalidTokenException;
import rsoi.lab2.gateway.model.FlightInfo;
import rsoi.lab2.gateway.model.TicketInfo;
import rsoi.lab2.gateway.redis.MessagePublisher;
import rsoi.lab2.gateway.redis.model.Task;
import rsoi.lab2.gateway.redis.model.Topic;

import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController
public class TicketController {

    private Logger logger = Logger.getLogger(TicketController.class.getName());

    private final MessagePublisher messagePublisher;

    @Value("${app.gatewayUuid}")
    private String gatewayUuid;

    @Value("${app.services.ticket}")
    private String ticketServiceUrl;

    @Value("${app.services.flight}")
    private String flightServiceUrl;

    @Autowired
    public TicketController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @GetMapping(value = "/pingTickets")
    public ResponseEntity<?> pingTickets(@RequestHeader(name = "Authorization") String accessToken,
                                         @RequestHeader(name = "User") String userUuid,
                                         @RequestHeader(name = "Service") String serviceUuid) {
        logger.info("Get request (pingTickets)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = ticketServiceUrl + "/ping?gatewayUuid=" + gatewayUuid;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        }
        else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/tickets")
    public ResponseEntity<?> getTickets(@RequestHeader(name = "Authorization") String accessToken,
                                        @RequestHeader(name = "User") String userUuid,
                                        @RequestHeader(name = "Service") String serviceUuid,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getTickets)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = ticketServiceUrl + "/tickets?page=" + page + "&size=" + size + "&gatewayUuid=" + gatewayUuid;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/userTickets")
    public ResponseEntity<?> getUserTickets(@RequestHeader(name = "Authorization") String accessToken,
                                        @RequestHeader(name = "User") String userUuid,
                                        @RequestHeader(name = "Service") String serviceUuid,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getUserTickets)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = ticketServiceUrl + "/userTickets?page=" + page + "&size=" + size + "&userUuid=" + userUuid + "&gatewayUuid=" + gatewayUuid;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/tickets",
            params = "uidFlight")
    public ResponseEntity<?> getTickets(@RequestHeader(name = "Authorization") String accessToken,
                                        @RequestHeader(name = "User") String userUuid,
                                        @RequestHeader(name = "Service") String serviceUuid,
                                        @RequestParam String uidFlight,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getTicketsByFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = ticketServiceUrl + "/flightTickets?uidFlight=" + uidFlight + "&page=" + page + "&size=" + size + "&gatewayUuid=" + gatewayUuid;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/countTickets")
    public ResponseEntity<?> countTickets(@RequestHeader(name = "Authorization") String accessToken,
                                          @RequestHeader(name = "User") String userUuid,
                                          @RequestHeader(name = "Service") String serviceUuid) {
        logger.info("Get request (countTickets)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = ticketServiceUrl + "/countAll?gatewayUuid=" + gatewayUuid;
            return restTemplate.getForEntity(resourceUrl, String.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/ticket",
            params = "uidTicket")
    public ResponseEntity<?> getTicket(@RequestHeader(name = "Authorization") String accessToken,
                                       @RequestHeader(name = "User") String userUuid,
                                       @RequestHeader(name = "Service") String serviceUuid,
                                       @RequestParam String uidTicket) {
        logger.info("Get request (getTicket)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = ticketServiceUrl + "/ticket?uidTicket=" + uidTicket + "&gatewayUuid=" + gatewayUuid;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @PutMapping(value = "/ticket")
    public ResponseEntity addTicket(@RequestHeader(name = "Authorization") String accessToken,
                                    @RequestHeader(name = "User") String userUuid,
                                    @RequestHeader(name = "Service") String serviceUuid,
                                    @RequestBody TicketInfo ticketInfo) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                logger.info("Get PUT request (addTicket)");
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String resourceUrl = flightServiceUrl + "/flight?uidFlight=" + ticketInfo.getUidFlight() + "&gatewayUuid=" + gatewayUuid;
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
                        ResponseEntity<String> responseTicket = restTemplate.exchange(ticketServiceUrl + "/ticket?gatewayUuid=" + gatewayUuid, HttpMethod.PUT, requestTicket, String.class);
                        if (responseTicket.getStatusCode().equals(HttpStatus.OK)) {
                            logger.info("Ticket created with uid " + responseTicket.getBody());
                            flightInfo.setNnTickets(flightInfo.getNnTickets() + 1);

                            String requestUrl = flightServiceUrl + "/flight?_method=patch" + "&gatewayUuid=" + gatewayUuid;
                            HttpEntity<FlightInfo> requestFlight;
                            try {
                                RestTemplate restTemplateFlight = new RestTemplate();
                                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                                requestFactory.setConnectTimeout(500);
                                requestFactory.setReadTimeout(500);
                                restTemplate.setRequestFactory(requestFactory);

                                requestFlight = new HttpEntity<>(flightInfo, headers);
                                restTemplateFlight.postForObject(requestUrl, requestFlight, ResponseEntity.class);
                            }
                            catch (ResourceAccessException ex) {
                                logger.info("Flight service is not available! Request added to the queue!");
                                messagePublisher.publish(
                                        Topic.TASK.getName(),
                                        new Task(requestUrl, flightInfo));
                            }
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
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @PatchMapping(value = "/ticket")
    public ResponseEntity editTicket(@RequestHeader(name = "Authorization") String accessToken,
                                     @RequestHeader(name = "User") String userUuid,
                                     @RequestHeader(name = "Service") String serviceUuid,
                                     @RequestBody TicketInfo ticketInfo) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                logger.info("Get PATCH request (editTicket)");
                RestTemplate restTemplate = new RestTemplate();
                String resourceUrl = ticketServiceUrl + "/ticket?_method=patch" + "&gatewayUuid=" + gatewayUuid;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                HttpEntity<TicketInfo> request = new HttpEntity<>(ticketInfo, headers);
                return restTemplate.postForObject(resourceUrl, request, ResponseEntity.class);
            } catch (Exception ex) {
                logger.info(ex.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
            }
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @DeleteMapping(value = "/ticket")
    public ResponseEntity deleteTicket(@RequestHeader(name = "Authorization") String accessToken,
                                       @RequestHeader(name = "User") String userUuid,
                                       @RequestHeader(name = "Service") String serviceUuid,
                                       @RequestBody String uidTicket) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                logger.info("Get DELETE request (deleteTicket)");
                //get ticket
                //save idFlight
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String resourceUrl = ticketServiceUrl + "/ticket?uidTicket=" + uidTicket + "&gatewayUuid=" + gatewayUuid;
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
                    resourceUrl = flightServiceUrl + "/flight?uidFlight=" + uidFlight + "&gatewayUuid=" + gatewayUuid;
                    ResponseEntity responseFlight = restTemplate.getForEntity(resourceUrl, String.class);
                    FlightInfo flightInfo = new Gson().fromJson(responseFlight.getBody().toString(), FlightInfo.class);
                    flightInfo.setNnTickets(flightInfo.getNnTickets() - 1);
                    new FlightController(messagePublisher).editFlight(flightInfo, gatewayUuid);
                    return responseFlight;
                } else {
                    logger.info("Server error while removing ticket");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while removing ticket");
                }
            } catch (Exception ex) {
                logger.info(ex.getLocalizedMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
            }
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @DeleteMapping(value = "/tickets")
    public ResponseEntity deleteTickets(@RequestHeader(name = "Authorization") String accessToken,
                                        @RequestHeader(name = "User") String userUuid,
                                        @RequestHeader(name = "Service") String serviceUuid,
                                        @RequestBody String uidFlight) {
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                logger.info("Get DELETE request (deleteTickets)");
                RestTemplate restTemplate = new RestTemplate();
                String resourceUrl = ticketServiceUrl + "/tickets?gatewayUuid=" + gatewayUuid;
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
        } else
            throw new InvalidTokenException("Token is invalid");
    }
}
