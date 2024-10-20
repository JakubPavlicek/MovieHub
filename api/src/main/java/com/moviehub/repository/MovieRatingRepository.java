package com.moviehub.repository;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Repository interface for managing MovieRating entities.
@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, UUID> {

    @Query("SELECT AVG(mr.rating) FROM MovieRating mr WHERE mr.movie.id = :movieId")
    Double getAverageRatingByMovieId(UUID movieId);

    Optional<MovieRating> findByMovieAndUser(Movie movie, User user);

}
