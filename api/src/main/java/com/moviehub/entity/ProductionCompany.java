package com.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Entity class representing a production company.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductionCompany {

    /// Unique identifier for the production company.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /// The name of the production company.
    @Column(
        length = 100,
        nullable = false,
        unique = true
    )
    private String name;

}
