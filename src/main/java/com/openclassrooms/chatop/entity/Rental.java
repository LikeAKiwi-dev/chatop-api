package com.openclassrooms.chatop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @Column(length = 255)
    private String name;

    @Getter
    @Setter
    @Column(length = 2000)
    private String description;

    @Getter
    @Setter
    private BigDecimal price;

    @Getter
    @Setter
    private BigDecimal surface;

    @Getter
    @Setter
    @Column(length = 255)
    private String picture;

    @Getter
    @Setter
    @Column(name = "owner_id")
    private Integer ownerId;

    @Getter
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Getter
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;
}
