package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.UserMapper;
import com.openclassrooms.chatop.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Integer id) {
        return userMapper.toDto(userService.findByIdOrThrow(id));
    }
}
