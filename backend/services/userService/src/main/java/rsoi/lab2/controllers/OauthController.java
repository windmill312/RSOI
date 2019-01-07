package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rsoi.lab2.entity.ExternalService;
import rsoi.lab2.payload.ApiResponse;
import rsoi.lab2.payload.ServiceRequest;
import rsoi.lab2.payload.ServiceResponse;
import rsoi.lab2.payload.SignUpServiceRequest;
import rsoi.lab2.repositories.ExternalServiceRepository;
import rsoi.lab2.security.JwtTokenProvider;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Value("${app.jwtAccessExpirationInMs}")
    private int jwtAccessExpirationInMs;

    //todo прописать адрес морды (авторизации)
    private String projectUrl;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ExternalServiceRepository externalServiceRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    /**
     * todo проверить редирект
     * todo на фронте проверять body на наличие RedirectUri
     **/
    @PostMapping("/signin")
    public ResponseEntity authenticateService(@Valid @RequestBody ServiceRequest serviceRequest) {

        if (externalServiceRepository.existsByUuid(serviceRequest.getUuid()))
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", projectUrl).body(serviceRequest);
        else
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Service not found!"));
    }

    @PostMapping("/signup")
    public ResponseEntity registerService(@Valid @RequestBody SignUpServiceRequest signUpServiceRequest) {
        if(externalServiceRepository.existsByName(signUpServiceRequest.getName())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Service name is already taken!"));
        }

        ExternalService externalService = new ExternalService(signUpServiceRequest.getName());
        ExternalService result = externalServiceRepository.save(externalService);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse(true, "Service registered successfully", result.getUuid(), result.getSecretKey()));
    }
}