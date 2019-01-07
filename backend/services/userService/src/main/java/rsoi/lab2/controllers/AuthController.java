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
import rsoi.lab2.entity.Role;
import rsoi.lab2.entity.Token;
import rsoi.lab2.entity.User;
import rsoi.lab2.exception.AppException;
import rsoi.lab2.model.RoleName;
import rsoi.lab2.model.TokenType;
import rsoi.lab2.payload.*;
import rsoi.lab2.repositories.RoleRepository;
import rsoi.lab2.repositories.TokenRepository;
import rsoi.lab2.repositories.UserRepository;
import rsoi.lab2.security.JwtTokenProvider;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${app.jwtAccessExpirationInMs}")
    private int jwtAccessExpirationInMs;

    @Value("${app.ourServiceUuid}")
    private String ourServiceUuid;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping(value = "/user/{username}")
    public ResponseEntity<?> isUserExists(@PathVariable String username) {

        logger.info("Get isUserExists with: " + username);
        if (userRepository.existsByEmailOrUsername(username, username))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        else
            return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Transactional
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String headerAuth, @RequestBody LoginRequest loginRequest) {

        logger.info("Get refresh-token request from: " + loginRequest.getServiceUuid() + "\n");

        User user = userRepository.findByUsernameOrEmail(loginRequest.getIdentifier(), loginRequest.getIdentifier()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email: " + loginRequest.getIdentifier())
        );

        boolean contains = tokenRepository.existsByUserAndServiceUuidAndValue(user, loginRequest.getServiceUuid(), headerAuth);

        if (contains) {

            tokenRepository.deleteAllByUserAndServiceUuid(user, loginRequest.getServiceUuid());

            String jwtAccess = tokenProvider.generateToken(user.getEmail(), user.getUuid().toString());
            String jwtRefresh = tokenProvider.generateToken(user.getEmail(), user.getUuid().toString());

            Token refreshToken = new Token();
            refreshToken.setServiceUuid(loginRequest.getServiceUuid());
            refreshToken.setUser(user);
            refreshToken.setTokenType(TokenType.REFRESH_TOKEN);
            refreshToken.setValue(jwtRefresh);
            tokenRepository.save(refreshToken);

            userRepository.save(user);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwtAccess, jwtRefresh, jwtAccessExpirationInMs));
        }
        else
        {
            throw new UsernameNotFoundException("Token is invalid");
        }
    }

    @Transactional
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Get auth request from: " + loginRequest.getServiceUuid() + "\n");

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

        tokenRepository.deleteAllByUserAndServiceUuid(user, loginRequest.getServiceUuid());

        String jwtAccess = tokenProvider.generateToken(loginRequest.getIdentifier(), user.getUuid().toString());
        String jwtRefresh = tokenProvider.generateToken(loginRequest.getIdentifier(), user.getUuid().toString());

        Token token1 = new Token();
        token1.setServiceUuid(loginRequest.getServiceUuid());
        token1.setUser(user);
        token1.setTokenType(TokenType.ACCESS_TOKEN);
        token1.setValue(jwtAccess);
        tokenRepository.save(token1);

        Token token2 = new Token();
        token2.setServiceUuid(loginRequest.getServiceUuid());
        token2.setUser(user);
        token2.setTokenType(TokenType.REFRESH_TOKEN);
        token2.setValue(jwtRefresh);
        tokenRepository.save(token2);

        userRepository.save(user);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtAccess, jwtRefresh, jwtAccessExpirationInMs));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validation (@RequestBody UserRequest userRequest) {

        User user = userRepository.findByUuid(userRequest.getUserUuid())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with UUID : " + userRequest.getUserUuid())
                );
        if (tokenRepository.existsByUserAndServiceUuidAndValue(user, userRequest.getServiceUuid(), userRequest.getToken()))
            return new ResponseEntity<>(new ApiResponse(true,"Token is valid"), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ApiResponse(false,"Token is invalid"), HttpStatus.FORBIDDEN);

    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpUserRequest signUpUserRequest) {
        if(userRepository.existsByUsername(signUpUserRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpUserRequest.getEmail())) {
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
}