package com.openclassrooms.chatop.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record RentalResponse(
        Integer id,
        String name,
        BigDecimal surface,
        BigDecimal price,
        String picture,
        String description,
        Integer owner_id,
        Instant created_at,
        Instant updated_at
) {}
