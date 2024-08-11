package com.movie_manager.repository;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.ProductionCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>, JpaSpecificationExecutor<Movie> {

    Page<Movie> findAllByDirector(Director director, Pageable pageable);

    Page<Movie> findAllByProductionContaining(ProductionCompany company, Pageable pageable);

    Page<Movie> findAllByGenresContaining(Genre genre, Pageable pageable);

    Page<Movie> findAllByCountriesContaining(Country country, Pageable pageable);

    @Query("SELECT movie FROM Movie movie INNER JOIN movie.cast movieCast WHERE movieCast.actor = :actor")
    Page<Movie> findAllByActorsContaining(Actor actor, Pageable pageable);

}
