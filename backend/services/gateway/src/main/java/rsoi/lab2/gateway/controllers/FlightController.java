package rsoi.lab2.gateway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.common.CheckToken;
import rsoi.lab2.gateway.exception.InvalidTokenException;
import rsoi.lab2.gateway.model.FlightInfo;

import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController
public class FlightController {

    private Logger logger = Logger.getLogger(FlightController.class.getName());

    @GetMapping(value = "/pingFlights")
    public ResponseEntity<?> pingFlights(@RequestHeader(name = "Authentication") String accessToken,
                                         @RequestHeader(name = "User") String userUuid,
                                         @RequestHeader(name = "Service") String serviceUuid) {
        logger.info("Get request (pingFlights)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8083/ping";
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/flights")
    public ResponseEntity<?> getFlights(@RequestHeader(name = "Authentication") String accessToken,
                                        @RequestHeader(name = "User") String userUuid,
                                        @RequestHeader(name = "Service") String serviceUuid,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8083/flights?page=" + page + "&size=" + size;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/flights",
            params = "uidRoute")
    public ResponseEntity<?> getFlightsByRoute(@RequestHeader(name = "Authentication") String accessToken,
                                               @RequestHeader(name = "User") String userUuid,
                                               @RequestHeader(name = "Service") String serviceUuid,
                                               @RequestParam String uidRoute,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getFlights)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8083/flights?uidRoute=" + uidRoute + "&page=" + page + "&size=" + size;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/countFlights")
    public ResponseEntity<?> countFlights(@RequestHeader(name = "Authentication") String accessToken,
                                          @RequestHeader(name = "User") String userUuid,
                                          @RequestHeader(name = "Service") String serviceUuid) {
        logger.info("Get request (countFlights)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8083/countAll";
            return restTemplate.getForEntity(resourceUrl, String.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @GetMapping(value = "/flight",
            params = {"uidFlight"})
    public ResponseEntity<?> getFlight(@RequestHeader(name = "Authentication") String accessToken,
                                       @RequestHeader(name = "User") String userUuid,
                                       @RequestHeader(name = "Service") String serviceUuid,
                                       @RequestParam String uidFlight) {
        logger.info("Get request (getFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8083/flight?uidFlight=" + uidFlight;
            return restTemplate.getForEntity(resourceUrl, Object.class);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @PutMapping(value = "/flight")
    public ResponseEntity addFlight(@RequestHeader(name = "Authentication") String accessToken,
                                    @RequestHeader(name = "User") String userUuid,
                                    @RequestHeader(name = "Service") String serviceUuid,
                                    @RequestBody FlightInfo flightInfo) {
        logger.info("Get PUT request (addFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            try {
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
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    @PatchMapping(value = "/flight")
    public ResponseEntity editFlight(@RequestHeader(name = "Authentication") String accessToken,
                                     @RequestHeader(name = "User") String userUuid,
                                     @RequestHeader(name = "Service") String serviceUuid,
                                     @RequestBody FlightInfo flightInfo) {

        logger.info("Get PATCH request (editFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
            return patchRequest(flightInfo);
        } else
            throw new InvalidTokenException("Token is invalid");
    }

    ResponseEntity editFlight(@RequestBody FlightInfo flightInfo) {

        logger.info("Get PATCH request (editFlight)");
        return patchRequest(flightInfo);
    }

    private ResponseEntity patchRequest(FlightInfo flightInfo) {
        try {
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

    @DeleteMapping(value = "/flight")
    public ResponseEntity deleteFlight(@RequestHeader(name = "Authentication") String accessToken,
                                       @RequestHeader(name = "User") String userUuid,
                                       @RequestHeader(name = "Service") String serviceUuid,
                                       @RequestBody String uidFlight) {
        logger.info("Get DELETE request (deleteFlight)");
        if (CheckToken.checkToken(accessToken, userUuid, serviceUuid)) {
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
        } else
            throw new InvalidTokenException("Token is invalid");
    }
}
