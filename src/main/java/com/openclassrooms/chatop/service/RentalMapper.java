package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.entity.Rental;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    private final String baseUrl;

    public RentalMapper(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public RentalResponse toDto(Rental r) {
        String picture = r.getPicture();

        if (picture != null && picture.startsWith("/")) {
            picture = baseUrl + picture;
        }

        return new RentalResponse(
                r.getId(),
                r.getName(),
                r.getSurface(),
                r.getPrice(),
                picture,
                r.getDescription(),
                r.getOwnerId(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}
