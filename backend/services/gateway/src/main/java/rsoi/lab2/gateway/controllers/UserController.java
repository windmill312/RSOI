package rsoi.lab2.gateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    @GetMapping(value = "/api/{username}")
    public ResponseEntity<?> isUserExists(@PathVariable String username) {
        logger.info("Get isUserExists with: " + username);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity("http://localhost:8084/api/auth/user/" + username, Object.class);
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
            ResponseEntity<?> response = restTemplate.exchange("http://localhost:8084/api/user/me", HttpMethod.GET, request, Object.class);
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
            ResponseEntity<?> response = restTemplate.exchange("http://localhost:8084/oauth?serviceUuid=" + serviceUuid, HttpMethod.GET, request, Object.class);
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
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/api/auth/refresh-token", request, Object.class);
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
        String resourceUrl = "http://localhost:8084/api/auth/validate";
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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Get auth request from: " + loginRequest.getServiceUuid() + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        try {
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/api/auth/signin", request, Object.class);
            if (loginRequest.getRedirectUri() == null)
                return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
            else {
                //todo create normal redirect
                return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
            }
               // return ResponseEntity.status(HttpStatus.FOUND).headers(response.getHeaders()).header("Location", loginRequest.getRedirectUri()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "User not found with username or email: " + loginRequest.getIdentifier() ),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/oauth/signin")
    public ResponseEntity<?> authenticateService(@Valid @RequestBody ServiceRequest serviceRequest) {

        logger.info("Get auth request with service uuid: " + serviceRequest.getUuid() + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<ServiceRequest> request = new HttpEntity<>(serviceRequest, headers);
        try {
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/oauth/signin", request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Service not found!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/oauth/signup")
    public ResponseEntity<?> registerService(@Valid @RequestBody SignUpServiceRequest signUpServiceRequest) throws JsonProcessingException {
        logger.info("Get register request with service name: " + signUpServiceRequest.getName() + "\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<SignUpServiceRequest> request = new HttpEntity<>(signUpServiceRequest, headers);
        try {
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/oauth/signup", request, Object.class);
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
            ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8084/api/auth/signup", request, Object.class);
            return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            logger.info("Troubles exists :(");
            //todo fix this foo
            return new ResponseEntity<>(new ApiResponse(false, "Email or username is already in use!"),
                    HttpStatus.UNAUTHORIZED);
        }
    }
}
