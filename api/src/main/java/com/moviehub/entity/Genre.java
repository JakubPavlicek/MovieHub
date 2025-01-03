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
/// Entity class representing a genre.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Genre {

    /// Unique identifier for the genre.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /// The name of the genre.
    @Column(
        length = 15,
        nullable = false,
        unique = true
    )
    private String name;

}
