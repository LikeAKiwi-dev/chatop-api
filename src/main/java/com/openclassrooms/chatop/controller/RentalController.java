package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.dto.RentalUpdateRequest;
import com.openclassrooms.chatop.dto.RentalsResponse;
import com.openclassrooms.chatop.service.RentalMapper;
import com.openclassrooms.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Rentals")
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(summary = "Lister les locations")
    @ApiResponse(
            responseCode = "200",
            description = "Liste des locations",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
            {
              "rentals": [
                {
                  "id": 1,
                  "name": "Appartement centre ville",
                  "surface": 50,
                  "price": 1200,
                  "picture": "http://localhost:3001/uploads/appartement.jpg",
                  "description": "Bel appartement lumineux",
                  "owner_id": 2
                }
              ]
            }
            """
                    )
            )
    )
    @GetMapping
    public RentalsResponse getAll() {
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        var rentals = rentalService.findAll().stream()
                .map(rentalMapper::toDto)
                .toList();

        return new RentalsResponse(rentals);
    }

    @Operation(summary = "Détail d'une location")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location trouvée"),
            @ApiResponse(responseCode = "404", description = "Location non trouvée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/{id}")
    public RentalResponse getById(@PathVariable Integer id) {
        return rentalMapper.toDto(rentalService.findByIdOrThrow(id));
    }

    @Operation(summary = "Créer une location")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Location créée",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"message\": \"Rental created !\" }"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleMessageResponse create(
            @Parameter(example = "Appartement centre ville") @RequestParam("name") String name,
            @Parameter(example = "50") @RequestParam("surface") BigDecimal surface,
            @Parameter(example = "1200") @RequestParam("price") BigDecimal price,
            @Parameter(example = "Bel appartement lumineux") @RequestParam("description") String description,
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

    @Operation(summary = "Mettre à jour une location")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location mise à jour"),
            @ApiResponse(responseCode = "404", description = "Location non trouvée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
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
