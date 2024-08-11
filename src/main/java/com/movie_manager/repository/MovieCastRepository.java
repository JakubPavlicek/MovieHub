package com.movie_manager.repository;

import com.movie_manager.entity.Movie;
import com.movie_manager.entity.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCastRepository extends JpaRepository<MovieCast, String> {

    void deleteAllByMovie(Movie movie);

}
