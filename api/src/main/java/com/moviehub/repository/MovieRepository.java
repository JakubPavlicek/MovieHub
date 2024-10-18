package com.moviehub.repository;

import com.moviehub.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID>, JpaSpecificationExecutor<Movie> {

    @Query("SELECT m.id FROM Movie m WHERE m.director.id = :directorId")
    Page<UUID> findMovieIdsByDirectorId(UUID directorId, Pageable pageable);

    @Query("SELECT m.id FROM Movie m JOIN m.production p WHERE p.id = :companyId")
    Page<UUID> findMovieIdsByCompanyId(UUID companyId, Pageable pageable);

    @Query("SELECT m.id FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    Page<UUID> findMovieIdsByGenreId(UUID genreId, Pageable pageable);

    @Query("SELECT m.id FROM Movie m JOIN m.countries c WHERE c.id = :countryId")
    Page<UUID> findMovieIdsByCountryId(UUID countryId, Pageable pageable);

    @Query("SELECT m.id FROM Movie m JOIN m.cast mc WHERE mc.actor.id = :actorId")
    Page<UUID> findMovieIdsByActorId(UUID actorId, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN FETCH m.genres WHERE m.id IN :movieIds")
    List<Movie> findMoviesWithGenresByIds(List<UUID> movieIds);

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

    @Query("SELECT m.id FROM Movie m")
    Page<UUID> findAllMovieIds(Pageable pageable);

    @Query("SELECT m.id FROM Movie m")
    Page<UUID> findAllMovieIds(Specification<Movie> specification, Pageable pageable);

}
