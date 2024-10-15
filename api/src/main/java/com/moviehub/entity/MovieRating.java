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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"movie_id", "user_id"},
        name = "uq_movie_user"
    )
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MovieRating {

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
        name = "user_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_user"),
        nullable = false
    )
    private User user;

    @Column(
        nullable = false
    )
    private Double rating;

}
