package com.movie_manager.repository;

import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.Movie_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>, JpaSpecificationExecutor<Movie> {

    @EntityGraph(attributePaths = {Movie_.DIRECTOR, Movie_.CAST, Movie_.PRODUCTION, Movie_.GENRES, Movie_.COUNTRIES})
    Page<Movie> findAllByGenresContaining(Genre genre, Pageable pageable);

}
