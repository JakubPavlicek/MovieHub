package com.movie_manager.repository;

import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>, JpaSpecificationExecutor<Movie> {

    Page<Movie> findAllByGenresContaining(Genre genre, Pageable pageable);

}
