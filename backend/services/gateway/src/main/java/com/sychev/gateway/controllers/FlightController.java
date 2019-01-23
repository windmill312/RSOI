package com.sychev.gateway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sychev.gateway.common.CheckToken;
import com.sychev.gateway.exception.InvalidTokenException;
import com.sychev.gateway.model.FlightInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import com.sychev.gateway.redis.MessagePublisher;
import com.sychev.gateway.redis.model.Task;
import com.sychev.gateway.redis.model.Topic;

import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController
public class FlightController {

    private Logger logger = Logger.getLogger(FlightController.class.getName());

    private final MessagePublisher messagePublisher;

    @Value("${app.gatewayUuid}")
    private String gatewayUuid;

    @Value("${app.services.flight}")
    private String flightServiceUrl;

    @Autowired
    public FlightController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @GetMapping(value = "/pingFlights")
    public ResponseEntity<?> pingFlights() {
        logger.info("Get request (pingFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = flightServiceUrl + "/ping?gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/flights")
    public ResponseEntity<?> getFlights(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = flightServiceUrl + "/flights?page=" + page + "&size=" + size + "&gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/flights",
            params = "uidRoute")
    public ResponseEntity<?> getFlightsByRoute(
            @RequestParam String uidRoute,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = flightServiceUrl + "/flights?uidRoute=" + uidRoute + "&page=" + page + "&size=" + size + "&gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(
            value = "/countFlights"
    )
    public ResponseEntity<?> countFlights() {
        logger.info("Get request (countFlights)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = flightServiceUrl + "/countAll?gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, String.class);
    }

    @GetMapping(value = "/flight",
            params = {"uidFlight"})
    public ResponseEntity<?> getFlight(
            @RequestParam String uidFlight) {
        logger.info("Get request (getFlight)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = flightServiceUrl + "/flight?uidFlight=" + uidFlight + "&gatewayUuid=" + gatewayUuid;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @PutMapping(value = "/flight")
    public ResponseEntity addFlight(@RequestHeader(name = "Authorization") String accessToken,
                                    @RequestHeader(name = "User") String userUuid,
                                    @RequestHeader(name = "Service") String serviceUuid,
                                    @RequestBody FlightInfo flightInfo) {
        logger.info("Get PUT request (addFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                String resourceUrl = flightServiceUrl + "/flight?gatewayUuid=" + gatewayUuid;
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
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @PatchMapping(value = "/flight")
    public ResponseEntity editFlight(@RequestHeader(name = "Authorization") String accessToken,
                                     @RequestHeader(name = "User") String userUuid,
                                     @RequestHeader(name = "Service") String serviceUuid,
                                     @RequestBody FlightInfo flightInfo) {

        logger.info("Get PATCH request (editFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            return patchRequest(flightInfo, gatewayUuid);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    void editFlight(@RequestBody FlightInfo flightInfo, String gatewayUuid) {

        logger.info("Get PATCH request (editFlight)");
        patchRequest(flightInfo, gatewayUuid);
    }

    private ResponseEntity patchRequest(FlightInfo flightInfo, String gatewayUuid) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = flightServiceUrl + "/flight?_method=patch&gatewayUuid=" + gatewayUuid;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<FlightInfo> request = new HttpEntity<>(flightInfo, headers);
        try {
            return restTemplate.postForObject(resourceUrl, request, ResponseEntity.class);
        } catch (ResourceAccessException ex) {
            logger.info(ex.getLocalizedMessage());
            messagePublisher.publish(
                    Topic.TASK.getName(),
                    new Task(resourceUrl, flightInfo));
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            logger.info(ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error because of exception (" + ex.getLocalizedMessage() + ")");
        }
    }

    @DeleteMapping(value = "/flight")
    public ResponseEntity deleteFlight(@RequestHeader(name = "Authorization") String accessToken,
                                       @RequestHeader(name = "User") String userUuid,
                                       @RequestHeader(name = "Service") String serviceUuid,
                                       @RequestBody String uidFlight) {
        logger.info("Get DELETE request (deleteFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid, gatewayUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8081/tickets?gatewayUuid=" + gatewayUuid;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> request = new HttpEntity<>(uidFlight, headers);
            ResponseEntity<String> responseTickets = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, request, String.class);
            if (responseTickets.getStatusCode() == HttpStatus.OK) {
                resourceUrl = flightServiceUrl + "/flight?gatewayUuid=" + gatewayUuid;
                HttpEntity<String> requestFlight = new HttpEntity<>(uidFlight, headers);
                ResponseEntity responseFlight = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestFlight, String.class);
                if (responseFlight.getStatusCode().equals(HttpStatus.OK))
                    return responseFlight;
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while flight removing");
            } else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service error while flight tickets removing");
        } else
            throw new InvalidTokenException("Token is invalid");
    }
}
