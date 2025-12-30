package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RentalUpdateRequest(
        @NotBlank String name,
        @NotNull BigDecimal surface,
        @NotNull BigDecimal price,
        @NotBlank String description
) {}
