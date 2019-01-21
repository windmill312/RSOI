package rsoi.lab2.gateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rsoi.lab2.gateway.common.*;
import rsoi.lab2.gateway.model.UserRequest;

import javax.validation.Valid;
import java.util.logging.Logger;

@CrossOrigin(maxAge = 3600)
@RestController("/user")
public class UserController {

    private Logger logger = Logger.getLogger(TicketController.class.getName());

    @Value("${app.gatewayUuid}")
    private String gatewayUuid;

    @GetMapping(value = "/api/{username}")
    public ResponseEntity<?> isUserExists(@PathVariable String username) {
        logger.info("Get isUserExists with: " + username);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity("http://localhost:8084/api/auth/user/" + username + "?gatewayUuid=" + gatewayUuid, Object.class);
    }

    @GetMapping("/api/me")
    public ResponseEntity getCurrentUser(@RequestHeader(name = "Authorization") String headerAuth) {
        logger.info("Get getCurrentUser request with access token: " + headerAuth + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", headerAuth);
        HttpEntity<SignUpServiceRequest> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://localhost:8084/api/user/me?gatewayUuid=" + gatewayUuid, HttpMethod.GET, request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        }
        catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Token is invalid or user not found!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(
            value = "/oauth",
            params = {
                    "serviceUuid"
            }
    )
    public ResponseEntity isServiceExists (@RequestHeader(name = "Authorization") String headerAuth, @RequestParam String serviceUuid) {
        logger.info("Get isServiceExists request with param: " + serviceUuid + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", headerAuth);
        HttpEntity<SignUpServiceRequest> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://localhost:8084/oauth?serviceUuid=" + serviceUuid + "&gatewayUuid=" + gatewayUuid, HttpMethod.GET, request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        }
        catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Token is invalid or user not found!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/auth/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String headerAuth, @RequestBody LoginRequest loginRequest) {

        logger.info("Get refresh-token request from: " + loginRequest.getServiceUuid() + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", headerAuth);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        try {
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/api/auth/refresh-token?gatewayUuid=" + gatewayUuid, request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        }
        catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Token is invalid or user not found!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/auth/validate")
    public ResponseEntity<?> validation(@RequestBody UserRequest userRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:8084/api/auth/validate?gatewayUuid=" + gatewayUuid;
        try {
            ResponseEntity response = restTemplate.postForObject(resourceUrl, userRequest, ResponseEntity.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        }
        catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Token is invalid!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/auth/signin")
    public Object authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Get auth request from: " + loginRequest.getServiceUuid() + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        try {
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/api/auth/signin?gatewayUuid=" + gatewayUuid, request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "User not found with username or email: " + loginRequest.getIdentifier() ),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(
            value = "/api/oauth/tokens",
            params = {
                    "serviceUuid",
                    "code",
                    "serviceSecret",
                    "redirectUri"
            }
    )
    public ResponseEntity<?> getTokensByCode(
            @RequestParam String serviceUuid,
            @RequestParam String code,
            @RequestParam String serviceSecret,
            @RequestParam String redirectUri
            ) {
        logger.info("GET request 'getTokensByCode' from service: " + serviceUuid);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(
                    "http://localhost:8084/oauth/tokens?code=" + code
                            + "&gatewayUuid=" + gatewayUuid
                            + "&serviceSecret=" + serviceSecret
                            + "&serviceUuid=" + serviceUuid,
                    Object.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return ResponseEntity.status(HttpStatus.OK).headers(response.getHeaders()).headers(response.getHeaders()).body(response.getBody());
            else
                return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Service not found!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/oauth/signin",
    params = {
            "serviceUuid",
            "redirectUri"
    })
    public ResponseEntity<?> authenticateService(@RequestParam String serviceUuid, @RequestParam String redirectUri) {

        logger.info("Get auth request with service uuid: " + serviceUuid + "\n");
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(
                    "http://localhost:8084/oauth/signin?serviceUuid=" + serviceUuid + "&redirectUri=" + redirectUri + "&gatewayUuid=" + gatewayUuid,
                    Object.class);
            return ResponseEntity.status(HttpStatus.FOUND).headers(response.getHeaders()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Service not found!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/oauth/signup")
    public ResponseEntity<?> registerService(@Valid @RequestBody SignUpServiceRequest signUpServiceRequest) {
        logger.info("Get register request with service name: " + signUpServiceRequest.getName() + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<SignUpServiceRequest> request = new HttpEntity<>(signUpServiceRequest, headers);
        try {
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/oauth/signup" + "&gatewayUuid=" + gatewayUuid, request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Service name is already taken!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpUserRequest signUpUserRequest) {
        logger.info("Get register request with username: " + signUpUserRequest.getUsername() + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<SignUpUserRequest> request = new HttpEntity<>(signUpUserRequest, headers);
        try {
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/api/auth/signup?gatewayUuid=" + gatewayUuid, request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Email or username is already in use!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }
}
