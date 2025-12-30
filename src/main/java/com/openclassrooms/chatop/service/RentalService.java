package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RentalUpdateRequest;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public Rental findByIdOrThrow(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RENTAL_NOT_FOUND"));
    }

    public Rental create(Rental rental) {
        return rentalRepository.save(rental);
    }

    public void update(Integer id, RentalUpdateRequest req) {
        Rental existing = findByIdOrThrow(id);

        existing.setName(req.name());
        existing.setSurface(req.surface());
        existing.setPrice(req.price());
        existing.setDescription(req.description());

        rentalRepository.save(existing);
    }
}
