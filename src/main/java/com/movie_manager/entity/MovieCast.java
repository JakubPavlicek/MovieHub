package com.movie_manager.entity;

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
import org.hibernate.annotations.UuidGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MovieCast {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String movieCastId;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "movie_id",
        referencedColumnName = "movieId",
        foreignKey = @ForeignKey(name = "fk_movie")
    )
    private Movie movie;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "actor_id",
        referencedColumnName = "actorId",
        foreignKey = @ForeignKey(name = "fk_actor")
    )
    private Actor actor;

    @Column(
        nullable = false
    )
    private String role;

}
