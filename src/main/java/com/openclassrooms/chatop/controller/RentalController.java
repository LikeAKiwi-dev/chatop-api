package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.dto.RentalUpdateRequest;
import com.openclassrooms.chatop.dto.RentalsResponse;
import com.openclassrooms.chatop.service.RentalMapper;
import com.openclassrooms.chatop.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.chatop.dto.SimpleMessageResponse;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final FileStorageService fileStorageService;

    public RentalController(RentalService rentalService, RentalMapper rentalMapper, FileStorageService fileStorageService) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public RentalsResponse getAll() {
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        var rentals = rentalService.findAll().stream()
                .map(r -> rentalMapper.toDto(r))
                .toList();

        return new RentalsResponse(rentals);
    }

    @GetMapping("/{id}")
    public RentalResponse getById(@PathVariable Integer id) {
        return rentalMapper.toDto(rentalService.findByIdOrThrow(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleMessageResponse create(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture,
            Authentication authentication
    ) {
        Integer ownerId = (Integer) authentication.getPrincipal();

        String picturePath = fileStorageService.store(picture);

        Rental r = new Rental();
        r.setName(name);
        r.setSurface(surface);
        r.setPrice(price);
        r.setDescription(description);
        r.setPicture(picturePath);
        r.setOwnerId(ownerId);

        rentalService.create(r);

        return new SimpleMessageResponse("Rental created !");
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public SimpleMessageResponse updateMultipart(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam BigDecimal surface,
            @RequestParam BigDecimal price,
            @RequestParam String description
    ) {
        RentalUpdateRequest req = new RentalUpdateRequest(
                name,
                surface,
                price,
                description
        );

        rentalService.update(id, req);
        return new SimpleMessageResponse("Rental updated !");
    }

}
