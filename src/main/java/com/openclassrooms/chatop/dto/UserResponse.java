package com.openclassrooms.chatop.dto;

import java.time.Instant;

public record UserResponse(
        Integer id,
        String name,
        String email,
        Instant created_at,
        Instant updated_at
) {}
