package com.sychev.user.controllers;

import com.sychev.user.entity.ExternalService;
import com.sychev.user.entity.ServiceKey;
import com.sychev.user.entity.Token;
import com.sychev.user.model.TokenType;
import com.sychev.user.payload.ApiResponse;
import com.sychev.user.payload.JwtAuthenticationResponse;
import com.sychev.user.payload.ServiceResponse;
import com.sychev.user.payload.SignUpServiceRequest;
import com.sychev.user.repositories.ServiceKeyRepository;
import com.sychev.user.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.sychev.user.repositories.ExternalServiceRepository;
import com.sychev.user.repositories.TokenRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Value("${app.gatewayUuid}")
    private String gateway;

    @Value("${app.frontUuid}")
    private String frontUuid;

    @Value("${app.jwtAccessExpirationInMs}")
    private int jwtAccessExpirationInMs;

    @Value("${app.frontUrl}")
    private String frontendUrl;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ExternalServiceRepository externalServiceRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    ServiceKeyRepository serviceKeyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping(
            value = "/signin",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity authenticateService(@RequestParam String serviceUuid,
                                              @RequestParam String redirectUri,
                                              @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway))
            if (externalServiceRepository.existsByUuid(UUID.fromString(serviceUuid))) {
                String url = frontendUrl + "/oauth?redirectUri=" + redirectUri + "&serviceUuid=" + serviceUuid;
                return ResponseEntity.status(HttpStatus.OK).header("Location", url).build();
            } else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(
            value = "/signup",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity registerService(@Valid @RequestBody SignUpServiceRequest signUpServiceRequest,
                                          @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway)) {
            if (externalServiceRepository.existsByName(signUpServiceRequest.getName())) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Service name is already taken!"));
            }

            ExternalService externalService = new ExternalService(signUpServiceRequest.getName());
            ExternalService result = externalServiceRepository.save(externalService);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse(true, "Service registered successfully", result.getUuid(), result.getSecretKey()));
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            params = {
                    "serviceUuid",
                    "gatewayUuid"
            })
    public ResponseEntity<?> isServiceExists(@RequestParam String serviceUuid,
                                             @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway)) {
            ExternalService externalService = externalServiceRepository.findByUuid(UUID.fromString(serviceUuid))
                    .orElseThrow(() -> new UsernameNotFoundException("Service not found with UUID : " + serviceUuid));

            SignUpServiceRequest signUpServiceRequest = new SignUpServiceRequest();
            signUpServiceRequest.setName(externalService.getName());

            return ResponseEntity.ok().body(signUpServiceRequest);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(
            value = "/tokens",
            params = {
                    "serviceUuid",
                    "gatewayUuid",
                    "serviceSecret",
                    "code"
            })
    public ResponseEntity<?> getTokensByCode(@RequestParam String serviceUuid,
                                             @RequestParam String gatewayUuid,
                                             @RequestParam String serviceSecret,
                                             @RequestParam String code
    ) {
        if (gatewayUuid.equals(gateway)) {

            ServiceKey serviceKey = serviceKeyRepository.findByValue(UUID.fromString(code))
                    .orElseThrow(() -> new NoSuchElementException("Code is invalid"));

            ExternalService externalService = externalServiceRepository.findByUuid(UUID.fromString(serviceUuid))
                    .orElseThrow(() -> new NoSuchElementException("Service not found"));

            if (externalService.getSecretKey().equals(serviceSecret)) {

                //todo передается uuid нашего фронта, надо заменить на uuid стороннего сервиса
                List<Token> tokens = tokenRepository.findAllByUserAndServiceUuid(serviceKey.getUser(), UUID.fromString(frontUuid)); //externalService.getUuid()

                if (tokens.size() <= 0)
                    throw new NoSuchElementException("There are no tokens for code");

                String accessToken = "";
                String refreshToken = "";

                for (Token token : tokens) {
                    if (token.getTokenType().equals(TokenType.ACCESS_TOKEN))
                        accessToken = token.getValue();
                    else
                        refreshToken = token.getValue();
                }

                JwtAuthenticationResponse response = new JwtAuthenticationResponse(accessToken, refreshToken, jwtAccessExpirationInMs, null);

                return ResponseEntity.ok().body(response);
            } else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Service secret is invalid!");
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
