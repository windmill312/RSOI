package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import rsoi.lab2.entity.User;
import rsoi.lab2.exception.AppException;
import rsoi.lab2.model.RoleName;
import rsoi.lab2.payload.ApiResponse;
import rsoi.lab2.payload.JwtAuthenticationResponse;
import rsoi.lab2.payload.LoginRequest;
import rsoi.lab2.payload.SignUpRequest;
import rsoi.lab2.repositories.RoleRepository;
import rsoi.lab2.repositories.UserRepository;
import rsoi.lab2.security.JwtTokenProvider;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String headerAuth, @RequestBody LoginRequest loginRequest) {

        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email: " + loginRequest.getUsernameOrEmail())
        );

        if (user.getRefreshToken().equals(headerAuth)) {

            String jwtAccess = tokenProvider.generateToken(user.getEmail());
            String jwtRefresh = tokenProvider.generateToken(user.getEmail());

            user.setAccessToken(jwtAccess);
            user.setRefreshToken(jwtRefresh);
            userRepository.save(user);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwtAccess, jwtRefresh, 30000));
        }
        else
        {
            throw new UsernameNotFoundException("Token is invalid");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtAccess = tokenProvider.generateToken(loginRequest.getUsernameOrEmail());
        String jwtRefresh = tokenProvider.generateToken(loginRequest.getUsernameOrEmail());

        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
                .orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + loginRequest.getUsernameOrEmail())
        );
        user.setAccessToken(jwtAccess);
        user.setRefreshToken(jwtRefresh);
        userRepository.save(user);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtAccess, jwtRefresh, 30000));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

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