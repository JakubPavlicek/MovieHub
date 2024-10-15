package com.moviehub.repository;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ProductionCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID>, JpaSpecificationExecutor<Movie> {

    Page<Movie> findAllByDirector(Director director, Pageable pageable);

    Page<Movie> findAllByProductionContaining(ProductionCompany company, Pageable pageable);

    Page<Movie> findAllByGenresContaining(Genre genre, Pageable pageable);

    Page<Movie> findAllByCountriesContaining(Country country, Pageable pageable);

    @Query("SELECT movie FROM Movie movie INNER JOIN movie.cast movieCast WHERE movieCast.actor = :actor")
    Page<Movie> findAllByActorsContaining(Actor actor, Pageable pageable);

    @Query("SELECT DISTINCT YEAR(m.releaseDate) FROM Movie m ORDER BY YEAR(m.releaseDate) DESC")
    List<Integer> findDistinctReleaseYears();

    @Query("SELECT m FROM Movie m JOIN FETCH m.director WHERE m.id = :movieId")
    Movie getMovieWithDirector(UUID movieId);

    @Query("SELECT m FROM Movie m JOIN FETCH m.cast mc JOIN FETCH mc.actor WHERE m = :movie")
    Movie getMovieWithCastAndActors(Movie movie);

    @Query("SELECT m FROM Movie m JOIN FETCH m.production WHERE m = :movie")
    Movie getMovieWithProduction(Movie movie);

    @Query("SELECT m FROM Movie m JOIN FETCH m.genres WHERE m = :movie")
    Movie getMovieWithGenres(Movie movie);

    @Query("SELECT m FROM Movie m JOIN FETCH m.countries WHERE m = :movie")
    Movie getMovieWithCountries(Movie movie);

}
