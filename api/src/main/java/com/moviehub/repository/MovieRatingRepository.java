package com.moviehub.repository;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, String> {

    @Query("SELECT AVG(mr.rating) FROM MovieRating mr WHERE mr.movie.id = :movieId")
    Double getAverageRatingByMovieId(String movieId);

    boolean existsByMovieAndUserId(Movie movie, String userId);

    void removeByMovieAndUserId(Movie movie, String userId);

}
