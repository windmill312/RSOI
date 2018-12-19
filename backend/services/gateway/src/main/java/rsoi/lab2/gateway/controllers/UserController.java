package rsoi.lab2.gateway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.model.UserInfo;

import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController
public class UserController {

    Logger logger = Logger.getLogger(TicketController.class.getName());

    @GetMapping(value = "/user",
            params = "uidUser")
    public ResponseEntity<?> getUser(@RequestParam String uidUser, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getUser)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8084/user?uidUser=" + uidUser + "&page=" + page + "&size=" + size;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<?> getUsers(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get request (getUsers)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8084/users?page=" + page + "&size=" + size;
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @GetMapping(value = "/pingUsers")
    public ResponseEntity<?> pingUsers() {
        logger.info("Get request (pingUsers)");
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8084/ping";
        return restTemplate.getForEntity(resourceUrl, Object.class);
    }

    @PutMapping(value = "/user")
    public ResponseEntity addUser(@RequestBody UserInfo userInfo) {
        try {
            logger.info("Get PUT request (addRoute)");
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String resourceUrl = "http://localhost:8084/users";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<UserInfo> request = new HttpEntity<>(userInfo, headers);
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
}
