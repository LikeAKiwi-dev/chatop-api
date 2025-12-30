package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
        @NotBlank String message,
        @NotNull Integer user_id,
        @NotNull Integer rental_id
) {}
