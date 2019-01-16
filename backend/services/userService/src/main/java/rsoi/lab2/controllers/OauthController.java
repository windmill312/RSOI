package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rsoi.lab2.entity.ExternalService;
import rsoi.lab2.payload.ApiResponse;
import rsoi.lab2.payload.ServiceRequest;
import rsoi.lab2.payload.ServiceResponse;
import rsoi.lab2.payload.SignUpServiceRequest;
import rsoi.lab2.repositories.ExternalServiceRepository;
import rsoi.lab2.security.JwtTokenProvider;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Value("${app.jwtAccessExpirationInMs}")
    private int jwtAccessExpirationInMs;

    @Value("${app.frontUrl}")
    private String projectUrl;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ExternalServiceRepository externalServiceRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping(value = "/signin")
    public ResponseEntity authenticateService(@RequestParam String serviceUuid, @RequestParam String redirectUri) {

        if (externalServiceRepository.existsByUuid(UUID.fromString(serviceUuid))) {
            String url = projectUrl + "/oauth?redirectUri=" + redirectUri + "&serviceUuid=" + serviceUuid;
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
        }
        else
            return ResponseEntity.notFound().build();
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

    @GetMapping(params = {
            "serviceUuid"
    })
    public ResponseEntity<?> isServiceExists (@RequestParam String serviceUuid) {

        ExternalService externalService = externalServiceRepository.findByUuid(UUID.fromString(serviceUuid))
                .orElseThrow(() ->new UsernameNotFoundException("Service not found with UUID : " + serviceUuid));

        SignUpServiceRequest signUpServiceRequest = new SignUpServiceRequest();
        signUpServiceRequest.setName(externalService.getName());

        return ResponseEntity.ok().body(signUpServiceRequest);
    }
}
