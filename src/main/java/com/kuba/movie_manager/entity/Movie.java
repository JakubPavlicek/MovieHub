package com.kuba.movie_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Movie {

    @Id
    @SequenceGenerator(
        name = "movie_id_generator",
        sequenceName = "movie_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        generator = "movie_id_generator",
        strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @Column(
        nullable = false,
        unique = true
    )
    private String movieId;

    @Column(
        length = 100,
        nullable = false
    )
    private String name;

    @Column(
        nullable = false
    )
    private Date release;

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
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_director")
    )
    private Director director;

    @ManyToMany
    @JoinTable(
        name = "movie_actor",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "actor_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_actor")
        )
    )
    private List<Actor> actors;

    @ManyToMany
    @JoinTable(
        name = "movie_genre",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "genre_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_genre")
        )
    )
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
        name = "movie_country",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "country_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_country")
        )
    )
    private List<Country> countries;
}
