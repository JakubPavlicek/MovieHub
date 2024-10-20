package com.moviehub.service;

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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
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
        log.info("adding movie");

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
        log.info("retrieving movie: {}", movieId);

        return movieRepository.findById(movieId)
                              .orElseThrow(() -> new MovieNotFoundException("Movie with ID: " + movieId + " not found"));
    }

    public Movie getMovieWithDetails(UUID movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException("Movie with ID: " + movieId + " not found");
        }

        log.info("retrieving movie with details: {}", movieId);

        // only fetch data that are needed
        Movie movie = movieRepository.getMovieWithDirector(movieId);
        movie = movieRepository.getMovieWithGenres(movie);
        movie = movieRepository.getMovieWithProduction(movie);
        movie = movieRepository.getMovieWithCastAndActors(movie);
        movie = movieRepository.getMovieWithCountries(movie);

        return movie;
    }

    public void deleteMovie(UUID movieId) {
        log.info("deleting movie: {}", movieId);

        Movie movie = getMovie(movieId);
        movieRepository.delete(movie);
    }

    public Movie updateMovie(UUID movieId, Movie incomingMovie) {
        log.info("updating movie: {}", movieId);

        Movie existingMovie = getMovie(movieId);

        updateMovieFields(existingMovie, incomingMovie);
        updateMovieRelatedEntities(existingMovie, incomingMovie);

        return movieRepository.save(existingMovie);
    }

    private static void updateMovieFields(Movie existingMovie, Movie incomingMovie) {
        log.debug("updating movie fields");

        existingMovie.setName(incomingMovie.getName());
        existingMovie.setFilename(incomingMovie.getFilename());
        existingMovie.setReleaseDate(incomingMovie.getReleaseDate());
        existingMovie.setDuration(incomingMovie.getDuration());
        existingMovie.setDescription(incomingMovie.getDescription());
        existingMovie.setPosterUrl(incomingMovie.getPosterUrl());
        existingMovie.setTrailerUrl(incomingMovie.getTrailerUrl());
    }

    private void updateMovieRelatedEntities(Movie existingMovie, Movie incomingMovie) {
        log.debug("updating movie related entities");

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
        log.debug("updating director");

        Director savedDirector = crewService.getSavedDirector(incomingMovie.getDirector());
        existingMovie.setDirector(savedDirector);
    }

    private void updateCast(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getCast() == null || incomingMovie.getCast().isEmpty()) {
            return;
        }

        log.debug("updating cast");

        crewService.deleteAllMovieCastsByMovie(existingMovie);
        List<MovieCast> savedMovieCasts = crewService.getSavedMovieCasts(incomingMovie.getCast(), existingMovie);
        existingMovie.setCast(savedMovieCasts);
    }

    private void updateProduction(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getProduction() == null || incomingMovie.getProduction().isEmpty()) {
            return;
        }

        log.debug("updating production companies");

        List<ProductionCompany> savedProduction = metadataService.getSavedProductions(incomingMovie.getProduction());
        existingMovie.setProduction(savedProduction);
    }

    private void updateGenres(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getGenres() == null || incomingMovie.getGenres().isEmpty()) {
            return;
        }

        log.debug("updating genres");

        List<Genre> savedGenres = metadataService.getSavedGenres(incomingMovie.getGenres());
        existingMovie.setGenres(savedGenres);
    }

    private void updateCountries(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getCountries() == null || incomingMovie.getCountries().isEmpty()) {
            return;
        }

        log.debug("updating countries");

        List<Country> savedCountries = metadataService.getSavedCountries(incomingMovie.getCountries());
        existingMovie.setCountries(savedCountries);
    }

    public Page<Movie> getMoviesWithGenre(UUID genreId, Integer page, Integer limit) {
        log.info("retrieving movies with genre: {}, page: {}, limit: {}", genreId, page, limit);

        metadataService.getGenre(genreId);
        return searchService.getMoviesWithGenre(genreId, page, limit);
    }

    public Page<Movie> getMoviesWithCountry(UUID countryId, Integer page, Integer limit) {
        log.info("retrieving movies with country: {}, page: {}, limit: {}", countryId, page, limit);

        metadataService.getCountry(countryId);
        return searchService.getMoviesWithCountry(countryId, page, limit);
    }

    public Page<Movie> getMoviesWithProductionCompany(UUID companyId, Integer page, Integer limit) {
        log.info("retrieving movies with production company: {}, page: {}, limit: {}", companyId, page, limit);

        metadataService.getProductionCompany(companyId);
        return searchService.getMoviesWithProductionCompany(companyId, page, limit);
    }

    public Page<Movie> getMoviesWithDirector(UUID directorId, Integer page, Integer limit) {
        log.info("retrieving movies with director: {}, page: {}, limit: {}", directorId, page, limit);

        crewService.getDirector(directorId);
        return searchService.getMoviesWithDirector(directorId, page, limit);
    }

    public Page<Movie> getMoviesWithActor(UUID actorId, Integer page, Integer limit) {
        log.info("retrieving movies with actor: {}, page: {}, limit: {}", actorId, page, limit);

        crewService.getActor(actorId);
        return searchService.getMoviesWithActor(actorId, page, limit);
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
        log.info("adding rating: {} for movie: {}", rating, movieId);

        Movie movie = getMovie(movieId);

        boolean wasRatingUpdated = interactionService.saveRating(movie, rating);

        Double newRating = interactionService.calculateRating(movieId);

        movie.setRating(newRating);

        // user rated the movie for the first time
        if (!wasRatingUpdated) {
            log.debug("incrementing review count for movie: {}", movieId);
            movie.setReviewCount(movie.getReviewCount() + 1);
        }

        movieRepository.save(movie);
    }

    public Double getRating(UUID movieId) {
        log.info("retrieving rating for movie: {}", movieId);

        Movie movie = getMovie(movieId);
        return interactionService.getRating(movie);
    }

    public Page<Movie> getMovies(Integer page, Integer limit, String sort) {
        log.info("retrieving movies with page: {}, limit: {}, sort: {}", page, limit, sort);
        return searchService.getMovies(page, limit, sort);
    }

    public Page<Movie> filterMovies(Integer page, Integer limit, String sort, String releaseYear, String genre, String country) {
        log.info("filtering movies with page: {}, limit: {}, sort: {}, releaseYear: {}, genre: {}, country: {}", page, limit, sort, releaseYear, genre, country);
        return searchService.filterMovies(page, limit, sort, releaseYear, genre, country);
    }

    public Page<Movie> searchMovies(Integer page, Integer limit, String sort, String keyword) {
        log.info("searching movies with page: {}, limit: {}, sort: {}, keyword: {}", page, limit, sort, keyword);
        return searchService.searchMovies(page, limit, sort, keyword);
    }

    public List<Integer> getYears() {
        return movieRepository.findDistinctReleaseYears();
    }

}
