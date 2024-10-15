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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        Director savedDirector = crewService.getSavedDirector(movie.getDirector());
        movie.setDirector(savedDirector);

        List<Country> savedCountries = metadataService.getSavedCountries(movie.getCountries());
        movie.setCountries(savedCountries);

        List<Genre> savedGenres = metadataService.getSavedGenres(movie.getGenres());
        movie.setGenres(savedGenres);

        List<ProductionCompany> savedProduction = metadataService.getSavedProductions(movie.getProduction());
        movie.setProduction(savedProduction);

        // Movie has to be persisted so it can be used in MovieCast
        Movie savedMovie = movieRepository.save(movie);

        List<MovieCast> savedCast = crewService.getSavedMovieCasts(movie.getCast(), savedMovie);
        savedMovie.setCast(savedCast);

        return savedMovie;
    }

    public Movie getMovie(UUID movieId) {
        return movieRepository.findById(movieId)
                              .orElseThrow(() -> new MovieNotFoundException("Movie with ID: " + movieId + " not found"));
    }

    public Movie getMovieWithDetails(UUID movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException("Movie with ID: " + movieId + " not found");
        }

        // only fetch data that are needed
        Movie movie = movieRepository.getMovieWithDirector(movieId);
        movie = movieRepository.getMovieWithGenres(movie);
        movie = movieRepository.getMovieWithProduction(movie);
        movie = movieRepository.getMovieWithCastAndActors(movie);
        movie = movieRepository.getMovieWithCountries(movie);

        return movie;
    }

    public void deleteMovie(UUID movieId) {
        Movie movie = getMovie(movieId);
        movieRepository.delete(movie);
    }

    public Movie updateMovie(UUID movieId, Movie incomingMovie) {
        Movie existingMovie = getMovie(movieId);

        updateMovieFields(existingMovie, incomingMovie);
        updateMovieRelatedEntities(existingMovie, incomingMovie);

        return movieRepository.save(existingMovie);
    }

    private static void updateMovieFields(Movie existingMovie, Movie incomingMovie) {
        existingMovie.setName(incomingMovie.getName());
        existingMovie.setFilename(incomingMovie.getFilename());
        existingMovie.setReleaseDate(incomingMovie.getReleaseDate());
        existingMovie.setDuration(incomingMovie.getDuration());
        existingMovie.setDescription(incomingMovie.getDescription());
        existingMovie.setPosterUrl(incomingMovie.getPosterUrl());
        existingMovie.setTrailerUrl(incomingMovie.getTrailerUrl());
    }

    private void updateMovieRelatedEntities(Movie existingMovie, Movie incomingMovie) {
        updateDirector(existingMovie, incomingMovie);
        updateCast(existingMovie, incomingMovie);
        updateProduction(existingMovie, incomingMovie);
        updateGenres(existingMovie, incomingMovie);
        updateCountries(existingMovie, incomingMovie);
    }

    private void updateDirector(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getDirector() == null) {
            return;
        }

        Director savedDirector = crewService.getSavedDirector(incomingMovie.getDirector());
        existingMovie.setDirector(savedDirector);
    }

    private void updateCast(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getCast() == null || incomingMovie.getCast().isEmpty()) {
            return;
        }

        crewService.deleteAllMovieCastsByMovie(existingMovie);
        List<MovieCast> savedMovieCasts = crewService.getSavedMovieCasts(incomingMovie.getCast(), existingMovie);
        existingMovie.setCast(savedMovieCasts);
    }

    private void updateProduction(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getProduction() == null || incomingMovie.getProduction().isEmpty()) {
            return;
        }

        List<ProductionCompany> savedProduction = metadataService.getSavedProductions(incomingMovie.getProduction());
        existingMovie.setProduction(savedProduction);
    }

    private void updateGenres(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getGenres() == null || incomingMovie.getGenres().isEmpty()) {
            return;
        }

        List<Genre> savedGenres = metadataService.getSavedGenres(incomingMovie.getGenres());
        existingMovie.setGenres(savedGenres);
    }

    private void updateCountries(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getCountries() == null || incomingMovie.getCountries().isEmpty()) {
            return;
        }

        List<Country> savedCountries = metadataService.getSavedCountries(incomingMovie.getCountries());
        existingMovie.setCountries(savedCountries);
    }

    public Page<Movie> getMoviesWithGenre(UUID genreId, Integer page, Integer limit) {
        Genre genre = metadataService.getGenre(genreId);
        return searchService.getMoviesWithGenre(genre, page, limit);
    }

    public Page<Movie> getMoviesWithCountry(UUID countryId, Integer page, Integer limit) {
        Country country = metadataService.getCountry(countryId);
        return searchService.getMoviesWithCountry(country, page, limit);
    }

    public Page<Movie> getMoviesWithProductionCompany(UUID companyId, Integer page, Integer limit) {
        ProductionCompany productionCompany = metadataService.getProductionCompany(companyId);
        return searchService.getMoviesWithProductionCompany(productionCompany, page, limit);
    }

    public Page<Movie> getMoviesWithDirector(UUID directorId, Integer page, Integer limit) {
        Director director = crewService.getDirector(directorId);
        return searchService.getMoviesWithDirector(director, page, limit);
    }

    public Page<Movie> getMoviesWithActor(UUID actorId, Integer page, Integer limit) {
        Actor actor = crewService.getActor(actorId);
        return searchService.getMoviesWithActor(actor, page, limit);
    }

    public void addComment(UUID movieId, String text) {
        Movie movie = getMovie(movieId);

        interactionService.addComment(movie, text);
    }

    public Page<Comment> getComments(UUID movieId, Integer page, Integer limit, String sort) {
        getMovie(movieId);
        return interactionService.getComments(movieId, page, limit, sort);
    }

    public void addRating(UUID movieId, Double rating) {
        Movie movie = getMovie(movieId);

        boolean wasRatingUpdated = interactionService.saveRating(movie, rating);

        Double newRating = interactionService.calculateRating(movieId);

        movie.setRating(newRating);

        // user rated the movie for the first time
        if (!wasRatingUpdated) {
            movie.setReviewCount(movie.getReviewCount() + 1);
        }

        movieRepository.save(movie);
    }

    public Double getRating(UUID movieId) {
        Movie movie = getMovie(movieId);
        return interactionService.getRating(movie);
    }

    public Page<Movie> getMovies(Integer page, Integer limit, String sort) {
        return searchService.getMovies(page, limit, sort);
    }

    public Page<Movie> filterMovies(Integer page, Integer limit, String sort, String releaseYear, String genre, String country) {
        return searchService.filterMovies(page, limit, sort, releaseYear, genre, country);
    }

    public Page<Movie> searchMovies(Integer page, Integer limit, String sort, String keyword) {
        return searchService.searchMovies(page, limit, sort, keyword);
    }

    public List<Integer> getYears() {
        return movieRepository.findDistinctReleaseYears();
    }

}
