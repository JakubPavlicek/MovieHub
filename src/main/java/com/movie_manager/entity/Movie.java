package com.movie_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Movie {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String movieId;

    @Column(
        length = 100,
        nullable = false
    )
    private String name;

    @Column(
        nullable = false
    )
    @Temporal(TemporalType.DATE)
    private LocalDate releaseDate;

    @Column(
        nullable = false
    )
    private Integer length;

    @Column(
        length = 1000,
        nullable = false
    )
    private String description;

    @ManyToOne
    @JoinColumn(
        name = "director_id",
        referencedColumnName = "directorId",
        foreignKey = @ForeignKey(name = "fk_director")
    )
    private Director director;

    @ManyToMany(
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "movie_actor",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "movieId",
            foreignKey = @ForeignKey(name = "fk_movie")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "actor_id",
            referencedColumnName = "actorId",
            foreignKey = @ForeignKey(name = "fk_actor")
        )
    )
    private List<Actor> actors;

    @ManyToMany(
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "movie_genre",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "movieId",
            foreignKey = @ForeignKey(name = "fk_movie")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "genre_id",
            referencedColumnName = "genreId",
            foreignKey = @ForeignKey(name = "fk_genre")
        )
    )
    private List<Genre> genres;

    @ManyToMany(
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "movie_country",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "movieId",
            foreignKey = @ForeignKey(name = "fk_movie")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "country_id",
            referencedColumnName = "countryId",
            foreignKey = @ForeignKey(name = "fk_country")
        )
    )
    private List<Country> countries;

}
