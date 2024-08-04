package com.movie_manager.service;

import com.movie_manager.entity.Country;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.MovieCast;
import com.movie_manager.entity.ProductionCompany;
import com.movie_manager.exception.MovieNotFoundException;
import com.movie_manager.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    private final DirectorService directorService;
    private final MovieCastService movieCastService;
    private final ProductionCompanyService productionService;
    private final CountryService countryService;
    private final GenreService genreService;

    private final ParseService parseService;

    @Transactional
    public Movie addMovie(Movie movie) {
        Director savedDirector = directorService.getSavedDirector(movie.getDirector());
        movie.setDirector(savedDirector);

        List<Country> savedCountries = countryService.getSavedCountries(movie.getCountries());
        movie.setCountries(savedCountries);

        List<Genre> savedGenres = genreService.getSavedGenres(movie.getGenres());
        movie.setGenres(savedGenres);

        List<ProductionCompany> savedProduction = productionService.getSavedProduction(movie.getProduction());
        movie.setProduction(savedProduction);

        Movie savedMovie = movieRepository.save(movie);

        List<MovieCast> savedCast = movieCastService.getSavedMovieCasts(movie.getCast(), savedMovie);
        savedMovie.setCast(savedCast);

        return savedMovie;
    }

    @Transactional
    public Movie getMovie(String movieId) {
        return movieRepository.findById(movieId)
                              .orElseThrow(() -> new MovieNotFoundException("Movie with ID: " + movieId + " not found"));
    }

    @Transactional
    public void deleteMovie(String movieId) {
        Movie movie = getMovie(movieId);
        movieRepository.delete(movie);
    }

    @Transactional
    public Movie updateMovie(String movieId, Movie incomingMovie) {
        Movie existingMovie = getMovie(movieId);

        updateMovieFields(existingMovie, incomingMovie);
        updateMovieRelatedEntities(existingMovie, incomingMovie);

        return movieRepository.save(existingMovie);
    }

    private static void updateMovieFields(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getName() != null) {
            existingMovie.setName(incomingMovie.getName());
        }
        if (incomingMovie.getReleaseDate() != null) {
            existingMovie.setReleaseDate(incomingMovie.getReleaseDate());
        }
        if (incomingMovie.getDuration() != null) {
            existingMovie.setDuration(incomingMovie.getDuration());
        }
        if (incomingMovie.getDescription() != null) {
            existingMovie.setDescription(incomingMovie.getDescription());
        }
        if (incomingMovie.getPosterUrl() != null) {
            existingMovie.setPosterUrl(incomingMovie.getPosterUrl());
        }
        if (incomingMovie.getTrailerUrl() != null) {
            existingMovie.setTrailerUrl(incomingMovie.getTrailerUrl());
        }
    }

    private void updateMovieRelatedEntities(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getDirector() != null) {
            Director savedDirector = directorService.getSavedDirector(incomingMovie.getDirector());
            existingMovie.setDirector(savedDirector);
        }
        if (incomingMovie.getCast() != null && !incomingMovie.getCast().isEmpty()) {
            List<MovieCast> savedMovieCasts = movieCastService.getSavedMovieCasts(incomingMovie.getCast(), existingMovie);
            existingMovie.setCast(savedMovieCasts);
        }
        if (incomingMovie.getProduction() != null && !incomingMovie.getProduction().isEmpty()) {
            List<ProductionCompany> savedProduction = productionService.getSavedProduction(incomingMovie.getProduction());
            existingMovie.setProduction(savedProduction);
        }
        if (incomingMovie.getCountries() != null && !incomingMovie.getCountries().isEmpty()) {
            List<Country> savedCountries = countryService.getSavedCountries(incomingMovie.getCountries());
            existingMovie.setCountries(savedCountries);
        }
        if (incomingMovie.getGenres() != null && !incomingMovie.getGenres().isEmpty()) {
            List<Genre> savedGenres = genreService.getSavedGenres(incomingMovie.getGenres());
            existingMovie.setGenres(savedGenres);
        }
    }

    @Transactional
    public Page<Movie> getMovies(Integer page, Integer limit, String sort, String name, String releaseDate, String duration, String description, String director, List<String> actors, List<String> genres, List<String> countries) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseSort(sort));

        Specification<Movie> specification = Specification.where(parseService.parseName(name))
                                                          .and(parseService.parseReleaseDate(releaseDate))
                                                          .and(parseService.parseDuration(duration))
                                                          .and(parseService.parseDescription(description))
                                                          .and(parseService.parseDirector(director))
                                                          .and(parseService.parseActors(actors))
                                                          .and(parseService.parseGenres(genres))
                                                          .and(parseService.parseCountries(countries));

        return movieRepository.findAll(specification, pageable);
    }

    @Transactional
    public Page<Movie> getMoviesByGenre(Genre genre, Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseSort(sort));

        return movieRepository.findAllByGenresContaining(genre, pageable);
    }

}
