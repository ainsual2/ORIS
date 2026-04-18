package ru.itis.dis403.lab2_06.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.dis403.lab2_06.dto.AuthRequest;
import ru.itis.dis403.lab2_06.dto.AuthResponse;
import ru.itis.dis403.lab2_06.dto.RegisterRequest;
import ru.itis.dis403.lab2_06.model.User;
import ru.itis.dis403.lab2_06.repository.UserRepository;
import ru.itis.dis403.lab2_06.service.JWTService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.username(),
                        authRequest.password()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(authRequest.username());
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse("User already exists"));
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(registerRequest.role() != null ? registerRequest.role() : "USER");

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
