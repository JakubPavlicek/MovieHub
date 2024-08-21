package com.moviehub.repository;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCastRepository extends JpaRepository<MovieCast, String> {

    void deleteAllByMovie(Movie movie);

}
