package com.procureflow.controller;

import com.procureflow.dto.LoginRequest;
import com.procureflow.dto.LoginResponse;
import com.procureflow.entity.User;
import com.procureflow.repository.UserRepository;
import com.procureflow.service.CustomUserDetailsService;
import com.procureflow.service.JwtService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService,
            UserRepository userRepository) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        // 1. Authenticate email + password (throws BadCredentialsException / DisabledException on failure)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load authenticated user (kept for consistency with the existing flow)
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        // 3. Generate JWT
        String token = jwtService.generateToken(request.getEmail());

        // 4. Return JWT + basic profile info (email/name/role) instead of a bare string
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        LoginResponse response = new LoginResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole().getName()
        );

        return ResponseEntity.ok(response);
    }
}