package com.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Entity class representing a movie cast.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MovieCast {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "movie_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_movie"),
        nullable = false
    )
    private Movie movie;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "actor_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_actor"),
        nullable = false
    )
    private Actor actor;

    @Column(
        nullable = false,
        length = 100
    )
    private String role;

}
