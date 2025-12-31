package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.*;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.security.JwtService;
import com.openclassrooms.chatop.service.AuthService;
import com.openclassrooms.chatop.service.UserMapper;
import com.openclassrooms.chatop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Tag(name = "Authentication")
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

    @Operation(summary = "Enregistrement d'un utilisateur")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur enregistré",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"token\": \"eyJhbGciOiJIUzI1NiJ9.xxx.yyy\" }"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/register")
    public AuthResponse register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"name\": \"Jean Dupont\", \"email\": \"jean@test.com\", \"password\": \"password123\" }"
                            )
                    )
            )
            @Valid @RequestBody RegisterRequest req) {
        User user = authService.register(req);
        return new AuthResponse(jwtService.generateToken(user.getId()));
    }

    @Operation(summary = "Connexion d'un utilisateur")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token JWT retourné",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"token\": \"eyJhbGciOiJIUzI1NiJ9.xxx.yyy\" }"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public AuthResponse login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"email\": \"test@test.com\", \"password\": \"password123\" }"
                            )
                    )
            )
            @Valid @RequestBody LoginRequest req) {
        User user = authService.authenticate(req);
        return new AuthResponse(jwtService.generateToken(user.getId()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Récupérer les informations de l'utilisateur connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur récupéré",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"id\": \"15\",\"name\": \"test\",\"email\": \"test@test.com\", \"password\": \"password123\", \"'created_at'\": \"2025-12-30T16:04:32Z\" , \"'updated_at'\": \"2025-12-30T16:04:32Z\"}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        Integer userId = (Integer) authentication.getPrincipal();
        return userMapper.toDto(userService.findByIdOrThrow(userId));
    }
}
