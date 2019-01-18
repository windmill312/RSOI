package rsoi.lab2.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rsoi.lab2.entity.*;
import rsoi.lab2.exception.AppException;
import rsoi.lab2.model.RoleName;
import rsoi.lab2.model.TokenType;
import rsoi.lab2.payload.*;
import rsoi.lab2.repositories.*;
import rsoi.lab2.security.JwtTokenProvider;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${app.frontUuid}")
    private String frontUuid;

    @Value("${app.gatewayUuid}")
    private String gateway;

    @Value("${app.jwtAccessExpirationInMs}")
    private int jwtAccessExpirationInMs;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    ExternalServiceRepository serviceRepository;

    @Autowired
    ServiceKeyRepository serviceKeyRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping(value = "/user/{username}",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity<?> isUserExists(@PathVariable String username,
                                          @RequestParam String gatewayUuid) {
        logger.info("Get isUserExists with: " + username);
        if (gatewayUuid.equals(gateway))
            if (userRepository.existsByEmailOrUsername(username, username))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            else
                return ResponseEntity.status(HttpStatus.OK).build();
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @Transactional
    @PostMapping(
            value = "/refresh-token",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String headerAuth,
                                          @RequestBody LoginRequest loginRequest,
                                          @RequestParam String gatewayUuid) {

        logger.info("Get refresh-token request from: " + loginRequest.getServiceUuid() + "\n");
        if (gatewayUuid.equals(gateway)) {
            User user = userRepository.findByUsernameOrEmail(loginRequest.getIdentifier(), loginRequest.getIdentifier()).orElseThrow(() ->
                    new UsernameNotFoundException("User not found with username or email: " + loginRequest.getIdentifier())
            );

            boolean contains = tokenRepository.existsByUserAndServiceUuidAndValue(user, loginRequest.getServiceUuid(), headerAuth.substring(7));

            if (contains) {
                return tokensOperations(user, loginRequest);
            } else {
                throw new UsernameNotFoundException("Token is invalid");
            }
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private void updateToken(UUID serviceUuid, User user, TokenType tokenType, String tokenValue) {
        Token token = new Token();
        token.setServiceUuid(serviceUuid);
        token.setUser(user);
        token.setTokenType(tokenType);
        token.setValue(tokenValue);
        token.setDttmCreate(System.currentTimeMillis());
        tokenRepository.save(token);
    }

    @Transactional
    @PostMapping(
            value = "/signin",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              @RequestParam String gatewayUuid) {

        logger.info("Get auth request from: " + loginRequest.getServiceUuid() + "\n");
        if (gatewayUuid.equals(gateway)) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getIdentifier(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsernameOrEmail(loginRequest.getIdentifier(), loginRequest.getIdentifier())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email : " + loginRequest.getIdentifier())
                    );

            return tokensOperations(user, loginRequest);
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping(
            value = "/validate",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity<?> validation (@RequestBody UserRequest userRequest,
                                         @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway)) {
            User user = userRepository.findByUuid(userRequest.getUserUuid())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with UUID : " + userRequest.getUserUuid())
                    );
            Token token = tokenRepository.findByUserAndServiceUuidAndValueAndTokenType(user, userRequest.getServiceUuid(), userRequest.getToken().substring(7), TokenType.ACCESS_TOKEN)
                    .orElseThrow(() -> new UsernameNotFoundException("Token not found"));
            if (token.getDttmCreate() + jwtAccessExpirationInMs > System.currentTimeMillis())
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<?> tokensOperations(User user, LoginRequest loginRequest) {

        ExternalService externalService = serviceRepository.findByUuid(loginRequest.getServiceUuid())
                .orElseThrow(() -> new NoSuchElementException("Service not found"));

        if (externalService.getUuid().equals(frontUuid)) {
            tokenRepository.deleteAllByUserAndServiceUuid(user, loginRequest.getServiceUuid());

            String jwtAccess = tokenProvider.generateToken(loginRequest.getIdentifier(), user.getUuid(), externalService.getSecretKey());
            String jwtRefresh = tokenProvider.generateToken(loginRequest.getIdentifier(), user.getUuid(), externalService.getSecretKey());

            updateToken(loginRequest.getServiceUuid(), user, TokenType.ACCESS_TOKEN, jwtAccess);
            updateToken(loginRequest.getServiceUuid(), user, TokenType.REFRESH_TOKEN, jwtRefresh);
            userRepository.save(user);

            return createResponseWithTokens(user, jwtAccess, jwtRefresh);
        }
        else
            return createResponseWithCode(externalService, user);

    }

    private ResponseEntity createResponseWithTokens(User user, String jwtAccess, String jwtRefresh) {

        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwtAccess, jwtRefresh, jwtAccessExpirationInMs);
        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName(RoleName.ROLE_ADMIN);

        for (Role role : user.getRoles())
            if (role.getName().equals(RoleName.ROLE_ADMIN))
                response.setAdmin(true);
            return ResponseEntity.ok(response);
    }

    private ResponseEntity createResponseWithCode(ExternalService externalService, User user) {

        UUID authorizationCode = UUID.randomUUID();
        ServiceKey serviceKey = new ServiceKey();
        serviceKey.setService(externalService);
        serviceKey.setUser(user);
        serviceKey.setValue(authorizationCode);

        externalService.getKeys().add(serviceKey);
        user.getKeys().add(serviceKey);

        userRepository.save(user);
        serviceRepository.save(externalService);

        String response = "{code: " + authorizationCode + "}";
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/signup",
            params = {
                    "gatewayUuid"
            })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpUserRequest signUpUserRequest,
                                          @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway)) {
            if (userRepository.existsByUsername(signUpUserRequest.getUsername())) {
                return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                        HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByEmail(signUpUserRequest.getEmail())) {
                return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                        HttpStatus.BAD_REQUEST);
            }

            User user = new User(signUpUserRequest.getUsername(), signUpUserRequest.getUsername(),
                    signUpUserRequest.getEmail(), signUpUserRequest.getPassword());

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));

            user.setRoles(Collections.singleton(userRole));

            User result = userRepository.save(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/users/{username}")
                    .buildAndExpand(result.getUsername()).toUri();

            return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}