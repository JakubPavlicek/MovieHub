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
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String id;

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
    private Integer duration;

    @Column(
        length = 1000,
        nullable = false
    )
    private String description;

    @Column(
        nullable = false
    )
    @Builder.Default
    private Double rating = 0.0;

    @Column(
        nullable = false
    )
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(
        nullable = false
    )
    private String posterUrl;

    @Column(
        nullable = false
    )
    private String trailerUrl;

    @ManyToOne
    @JoinColumn(
        name = "director_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_director")
    )
    private Director director;

    @OneToMany(
        mappedBy = "movie",
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE
    )
    private List<MovieCast> cast;

    @ManyToMany(
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "movie_production",
        joinColumns = @JoinColumn(
            name = "movie_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_movie")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "company_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_company")
        )
    )
    private List<ProductionCompany> production;

    @ManyToMany(
        fetch = FetchType.EAGER
    )
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

    @ManyToMany(
        fetch = FetchType.EAGER
    )
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

    @UpdateTimestamp
    @Column(
        nullable = false
    )
    private LocalDateTime updatedAt;

}
