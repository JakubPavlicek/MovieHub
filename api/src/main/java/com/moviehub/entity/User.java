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

    @Id
    private String id;

    @Column(
        nullable = false
    )
    private String name;

    @Column(
        length = 320,
        nullable = false
    )
    private String email;

    @Column(
        nullable = false
    )
    private String pictureUrl;

}
