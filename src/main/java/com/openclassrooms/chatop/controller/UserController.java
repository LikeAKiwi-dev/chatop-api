package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.UserMapper;
import com.openclassrooms.chatop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Détail d'un utilisateur")
    @ApiResponse(
            responseCode = "200",
            description = "Utilisateur trouvé",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
            {
              "id": 1,
              "name": "Jean Dupont",
              "email": "jean@test.com",
              "created_at": "2024-01-10"
            }
            """
                    )
            )
    )
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Integer id) {
        return userMapper.toDto(userService.findByIdOrThrow(id));
    }
}
