package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.*;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.security.JwtService;
import com.openclassrooms.chatop.service.AuthService;
import com.openclassrooms.chatop.service.UserMapper;
import com.openclassrooms.chatop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            UserService userService,
            UserMapper userMapper
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userMapper = userMapper;
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

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        return userMapper.toDto(userService.findByIdOrThrow(userId));
    }
}
