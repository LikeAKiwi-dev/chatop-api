package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.*;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.security.JwtService;
import com.openclassrooms.chatop.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        User user = authService.register(req);
        return new AuthResponse(jwtService.generateToken(user.getId()));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        User user = authService.authenticate(req);
        return new AuthResponse(jwtService.generateToken(user.getId()));
    }
}
