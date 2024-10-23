package com.moviehub.entity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Entity class representing a movie.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Movie {

    /// Unique identifier for the movie.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /// The name of the movie.
    @Column(
        length = 100,
        nullable = false
    )
    private String name;

    /// The filename of the movie's media file.
    @Column(
        nullable = false
    )
    private String filename;

    /// The release date of the movie.
    @Column(
        nullable = false
    )
    @Temporal(TemporalType.DATE)
    private LocalDate releaseDate;

    /// The duration of the movie in minutes.
    @Column(
        nullable = false
    )
    private Integer duration;

    /// A brief description of the movie.
    @Column(
        nullable = false
    )
    private String description;

    /// The average rating of the movie.
    @Column(
        nullable = false
    )
    @Builder.Default
    private Double rating = 0.0;

    /// The count of reviews for the movie.
    @Column(
        nullable = false
    )
    @Builder.Default
    private Integer reviewCount = 0;

    /// The URL for the movie's poster image.
    @Column(
        nullable = false
    )
    private String posterUrl;

    /// The URL for the movie's trailer.
    @Column(
        nullable = false
    )
    private String trailerUrl;

    /// The director of the movie.
    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "director_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_director"),
        nullable = false
    )
    private Director director;

    /// The list of comments associated with the movie.
    @OneToMany(
        mappedBy = "movie",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    @BatchSize(size = 10)
    private List<Comment> comments;

    /// The list of cast members associated with the movie.
    @OneToMany(
        mappedBy = "movie",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    @BatchSize(size = 5)
    private List<MovieCast> cast;

    /// The list of production companies involved in the movie.
    @ManyToMany(
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "movie_production",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie"),
            nullable = false
        ),
        inverseJoinColumns = @JoinColumn(
            name = "company_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_company"),
            nullable = false
        )
    )
    @BatchSize(size = 5)
    private List<ProductionCompany> production;

    /// The list of genres associated with the movie.
    @ManyToMany(
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "movie_genre",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie"),
            nullable = false
        ),
        inverseJoinColumns = @JoinColumn(
            name = "genre_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_genre"),
            nullable = false
        )
    )
    @BatchSize(size = 5)
    private List<Genre> genres;

    /// The list of countries associated with the movie.
    @ManyToMany(
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "movie_country",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie"),
            nullable = false
        ),
        inverseJoinColumns = @JoinColumn(
            name = "country_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_country"),
            nullable = false
        )
    )
    @BatchSize(size = 5)
    private List<Country> countries;

    /// The list of ratings associated with the movie.
    @OneToMany(
        mappedBy = "movie",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    private List<MovieRating> ratings;

    /// The timestamp for the last update of the movie entity.
    @UpdateTimestamp
    @Column(
        nullable = false
    )
    private LocalDateTime updatedAt;

    /// Sets the name of the movie.
    /// If the provided name is null, it will not be updated.
    ///
    /// @param newName the new name of the movie
    public void setName(String newName) {
        if (newName != null) {
            name = newName;
        }
    }

    /// Sets the release date of the movie.
    /// If the provided release date is null, it will not be updated.
    ///
    /// @param newReleaseDate the new release date of the movie
    public void setReleaseDate(LocalDate newReleaseDate) {
        if (newReleaseDate != null) {
            releaseDate = newReleaseDate;
        }
    }

    /// Sets the duration of the movie.
    /// If the provided duration is null, it will not be updated.
    ///
    /// @param newDuration the new duration of the movie in minutes
    public void setDuration(Integer newDuration) {
        if (newDuration != null) {
            duration = newDuration;
        }
    }

    /// Sets the description of the movie.
    /// If the provided description is null, it will not be updated.
    ///
    /// @param newDescription the new description of the movie
    public void setDescription(String newDescription) {
        if (newDescription != null) {
            description = newDescription;
        }
    }

    /// Sets the URL of the movie's poster.
    /// If the provided URL is null, it will not be updated.
    ///
    /// @param newPosterUrl the new poster URL of the movie
    public void setPosterUrl(String newPosterUrl) {
        if (newPosterUrl != null) {
            posterUrl = newPosterUrl;
        }
    }

    /// Sets the URL of the movie's trailer.
    /// If the provided URL is null, it will not be updated.
    ///
    /// @param newTrailerUrl the new trailer URL of the movie
    public void setTrailerUrl(String newTrailerUrl) {
        if (newTrailerUrl != null) {
            trailerUrl = newTrailerUrl;
        }
    }

}
