package com.movie_manager.repository;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.MovieCast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCastRepository extends JpaRepository<MovieCast, String> {

    void deleteAllByMovie(Movie movie);

    @Query("SELECT cast.movie FROM MovieCast cast WHERE cast.actor = :actor")
    Page<Movie> findMoviesByActor(Actor actor, Pageable pageable);

}
