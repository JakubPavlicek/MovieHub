package com.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
/// Entity class representing a director.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Director {

    /// Unique identifier for the director.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /// The name of the director.
    @Column(
        length = 50,
        nullable = false,
        unique = true
    )
    private String name;

    /// A brief biography of the director.
    private String bio;

    /// The gender of the director.
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender gender = Gender.UNSPECIFIED;

    /// Sets the name of the director.
    /// If the provided name is null, it will not be updated.
    ///
    /// @param newName the new name of the director
    public void setName(String newName) {
        if (newName != null) {
            name = newName;
        }
    }

    /// Sets the biography of the director.
    /// If the provided biography is null, it will not be updated.
    ///
    /// @param newBio the new biography of the director
    public void setBio(String newBio) {
        if (newBio != null) {
            bio = newBio;
        }
    }

    /// Sets the gender of the director.
    /// If the provided gender is null, it will not be updated.
    ///
    /// @param newGender the new gender of the director
    public void setGender(Gender newGender) {
        if (newGender != null) {
            gender = newGender;
        }
    }

}
