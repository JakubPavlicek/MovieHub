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
/// Entity class representing an actor.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Actor {

    /// Unique identifier for the actor.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /// The name of the actor.
    @Column(
        length = 50,
        nullable = false,
        unique = true
    )
    private String name;

    /// A brief biography of the actor.
    private String bio;

    /// The gender of the actor.
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender gender = Gender.UNSPECIFIED;

    /// Sets the name of the actor.
    /// If the provided name is null, it will not be updated.
    ///
    /// @param newName the new name of the actor
    public void setName(String newName) {
        if (newName != null) {
            name = newName;
        }
    }

    /// Sets the biography of the actor.
    /// If the provided biography is null, it will not be updated.
    ///
    /// @param newBio the new biography of the actor
    public void setBio(String newBio) {
        if (newBio != null) {
            bio = newBio;
        }
    }

    /// Sets the gender of the actor.
    /// If the provided gender is null, it will not be updated.
    ///
    /// @param newGender the new gender of the actor
    public void setGender(Gender newGender) {
        if (newGender != null) {
            gender = newGender;
        }
    }

}
