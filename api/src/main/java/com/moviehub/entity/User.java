package com.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Entity class representing a user.
@Entity
@Table(name = "auth_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    /// Unique identifier for the user (unique user's id from the Auth0).
    @Id
    private String id;

    /// The name of the user.
    @Column(
        nullable = false
    )
    private String name;

    /// The email address of the user.
    @Column(
        length = 320,
        nullable = false
    )
    private String email;

    /// The URL of the user's profile picture.
    @Column(
        nullable = false
    )
    private String pictureUrl;

}
