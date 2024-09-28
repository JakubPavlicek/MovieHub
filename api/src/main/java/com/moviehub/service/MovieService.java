package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.exception.MovieNotFoundException;
import com.moviehub.repository.MovieRepository;
import com.moviehub.security.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    private final MovieSearchService searchService;
    private final MovieCrewService crewService;
    private final MovieMetadataService metadataService;
    private final MovieInteractionService interactionService;

    public Movie addMovie(Movie movie) {
        AuthUser.getUserId();

        Director savedDirector = crewService.getSavedDirector(movie.getDirector());
        movie.setDirector(savedDirector);

        List<Country> savedCountries = metadataService.getSavedCountries(movie.getCountries());
        movie.setCountries(savedCountries);

        List<Genre> savedGenres = metadataService.getSavedGenres(movie.getGenres());
        movie.setGenres(savedGenres);

        List<ProductionCompany> savedProduction = metadataService.getSavedProductions(movie.getProduction());
        movie.setProduction(savedProduction);

        // Movie has to to exist in db so it can be used in MovieCast
        Movie savedMovie = movieRepository.save(movie);

        List<MovieCast> savedCast = crewService.getSavedMovieCasts(movie.getCast(), savedMovie);
        savedMovie.setCast(savedCast);

        return savedMovie;
    }

    public Movie getMovie(String movieId) {
        return movieRepository.findById(movieId)
                              .orElseThrow(() -> new MovieNotFoundException("Movie with ID: " + movieId + " not found"));
    }

    public void deleteMovie(String movieId) {
        Movie movie = getMovie(movieId);
        movieRepository.delete(movie);
    }

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
            Director savedDirector = crewService.getSavedDirector(incomingMovie.getDirector());
            existingMovie.setDirector(savedDirector);
        }
        if (incomingMovie.getCast() != null && !incomingMovie.getCast().isEmpty()) {
            crewService.deleteAllMovieCastsByMovie(existingMovie);

            List<MovieCast> savedMovieCasts = crewService.getSavedMovieCasts(incomingMovie.getCast(), existingMovie);
            existingMovie.setCast(savedMovieCasts);
        }
        if (incomingMovie.getProduction() != null && !incomingMovie.getProduction().isEmpty()) {
            List<ProductionCompany> savedProduction = metadataService.getSavedProductions(incomingMovie.getProduction());
            existingMovie.setProduction(savedProduction);
        }
        if (incomingMovie.getCountries() != null && !incomingMovie.getCountries().isEmpty()) {
            List<Country> savedCountries = metadataService.getSavedCountries(incomingMovie.getCountries());
            existingMovie.setCountries(savedCountries);
        }
        if (incomingMovie.getGenres() != null && !incomingMovie.getGenres().isEmpty()) {
            List<Genre> savedGenres = metadataService.getSavedGenres(incomingMovie.getGenres());
            existingMovie.setGenres(savedGenres);
        }
    }

    public Page<Movie> getMovies(Integer page, Integer limit, String sort, String name, String releaseDate, String duration, String description, String director, List<String> actors, List<String> genres, List<String> countries, String keyword) {
        return searchService.getMovies(page, limit, sort, name, releaseDate, duration, description, director, actors, genres, countries, keyword);
    }

    public Page<Movie> getMoviesWithGenre(String genreId, Integer page, Integer limit) {
        Genre genre = metadataService.getGenre(genreId);
        return searchService.getMoviesWithGenre(genre, page, limit);
    }

    public Page<Movie> getMoviesWithCountry(String countryId, Integer page, Integer limit) {
        Country country = metadataService.getCountry(countryId);
        return searchService.getMoviesWithCountry(country, page, limit);
    }

    public Page<Movie> getMoviesWithProductionCompany(String companyId, Integer page, Integer limit) {
        ProductionCompany productionCompany = metadataService.getProductionCompany(companyId);
        return searchService.getMoviesWithProductionCompany(productionCompany, page, limit);
    }

    public Page<Movie> getMoviesWithDirector(String directorId, Integer page, Integer limit) {
        Director director = crewService.getDirector(directorId);
        return searchService.getMoviesWithDirector(director, page, limit);
    }

    public Page<Movie> getMoviesWithActor(String actorId, Integer page, Integer limit) {
        Actor actor = crewService.getActor(actorId);
        return searchService.getMoviesWithActor(actor, page, limit);
    }

    public void addComment(String movieId, Comment comment) {
        Movie movie = getMovie(movieId);

        interactionService.saveComment(movie, comment);
    }

    public Page<Comment> getComments(String movieId, Integer page, Integer limit, String sort) {
        Movie movie = getMovie(movieId);
        return interactionService.getComments(movie, page, limit, sort);
    }

    public void updateRating(String movieId, Double rating) {
        Movie movie = getMovie(movieId);

        interactionService.saveRating(movie, rating);

        Double newRating = interactionService.calculateRating(movieId);

        movie.setRating(newRating);
        movie.setReviewCount(movie.getReviewCount() + 1);

        movieRepository.save(movie);
    }

}
