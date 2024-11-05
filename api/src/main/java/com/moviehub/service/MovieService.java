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

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing movie-related operations in the MovieHub application.
/// This class handles adding, retrieving, updating, deleting, and filtering movies,
/// as well as managing movie metadata, interactions, and comments.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MovieService {

    /// Repository for handling movie entities in the database.
    private final MovieRepository movieRepository;

    /// Service for handling movie search operations.
    private final MovieSearchService searchService;
    /// Service for managing movie crew operations.
    private final MovieCrewService crewService;
    /// Service for managing movie metadata.
    private final MovieMetadataService metadataService;
    /// Service for managing interactions with movies, such as ratings and comments.
    private final MovieInteractionService interactionService;

    /// Adds a new movie to the repository, saving related entities such as director,
    /// countries, genres, production companies, and cast.
    ///
    /// @param movie The Movie entity to be added.
    /// @return The saved Movie entity with updated related entities.
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

    /// Retrieves a movie by its unique identifier (UUID).
    ///
    /// @param movieId The UUID of the movie to retrieve.
    /// @return The Movie entity if found.
    /// @throws MovieNotFoundException if the movie is not found.
    public Movie getMovie(UUID movieId) {
        log.info("retrieving movie: {}", movieId);

        return movieRepository.findById(movieId)
                              .orElseThrow(() -> new MovieNotFoundException("Movie with ID: " + movieId + " not found"));
    }

    /// Retrieves a movie along with its detailed information such as director,
    /// genres, production companies, cast, and countries.
    ///
    /// @param movieId The UUID of the movie to retrieve.
    /// @return The Movie entity with all related details.
    /// @throws MovieNotFoundException if the movie is not found.
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

    /// Deletes a movie from the repository by its unique identifier (UUID).
    ///
    /// @param movieId The UUID of the movie to delete.
    public void deleteMovie(UUID movieId) {
        log.info("deleting movie: {}", movieId);

        Movie movie = getMovie(movieId);
        movieRepository.delete(movie);
    }

    /// Updates an existing movie with new information.
    ///
    /// @param movieId The UUID of the movie to update.
    /// @param incomingMovie The Movie entity containing updated information.
    /// @return The updated Movie entity.
    public Movie updateMovie(UUID movieId, Movie incomingMovie) {
        log.info("updating movie: {}", movieId);

        Movie existingMovie = getMovie(movieId);

        updateMovieFields(existingMovie, incomingMovie);
        updateMovieRelatedEntities(existingMovie, incomingMovie);

        return movieRepository.save(existingMovie);
    }

    /// Updates the fields of an existing movie with the provided incoming movie data.
    ///
    /// @param existingMovie The existing Movie entity to update.
    /// @param incomingMovie The Movie entity containing new field values.
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

    /// Updates related entities of the existing movie, such as director, cast,
    /// production companies, genres, and countries.
    ///
    /// @param existingMovie The existing Movie entity to update.
    /// @param incomingMovie The Movie entity containing new related entities.
    private void updateMovieRelatedEntities(Movie existingMovie, Movie incomingMovie) {
        log.debug("updating movie related entities");

        updateDirector(existingMovie, incomingMovie);
        updateCast(existingMovie, incomingMovie);
        updateProduction(existingMovie, incomingMovie);
        updateGenres(existingMovie, incomingMovie);
        updateCountries(existingMovie, incomingMovie);
    }

    /// Updates the director of the existing movie if a new director is provided.
    ///
    /// @param existingMovie The existing Movie entity to update.
    /// @param incomingMovie The Movie entity containing new director information.
    private void updateDirector(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getDirector() == null) {
            return;
        }
        log.debug("updating director");

        Director savedDirector = crewService.getSavedDirector(incomingMovie.getDirector());
        existingMovie.setDirector(savedDirector);
    }

    /// Updates the cast of the existing movie with the provided incoming cast data.
    ///
    /// @param existingMovie The existing Movie entity to update.
    /// @param incomingMovie The Movie entity containing new cast information.
    private void updateCast(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getCast() == null || incomingMovie.getCast().isEmpty()) {
            return;
        }

        log.debug("updating cast");

        crewService.deleteAllMovieCastsByMovie(existingMovie);
        List<MovieCast> savedMovieCasts = crewService.getSavedMovieCasts(incomingMovie.getCast(), existingMovie);
        existingMovie.setCast(savedMovieCasts);
    }

    /// Updates the production companies of the existing movie with the provided incoming production data.
    ///
    /// @param existingMovie The existing Movie entity to update.
    /// @param incomingMovie The Movie entity containing new production information.
    private void updateProduction(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getProduction() == null || incomingMovie.getProduction().isEmpty()) {
            return;
        }

        log.debug("updating production companies");

        List<ProductionCompany> savedProduction = metadataService.getSavedProductions(incomingMovie.getProduction());
        existingMovie.setProduction(savedProduction);
    }

    /// Updates the genres of the existing movie with the provided incoming genre data.
    ///
    /// @param existingMovie The existing Movie entity to update.
    /// @param incomingMovie The Movie entity containing new genre information.
    private void updateGenres(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getGenres() == null || incomingMovie.getGenres().isEmpty()) {
            return;
        }

        log.debug("updating genres");

        List<Genre> savedGenres = metadataService.getSavedGenres(incomingMovie.getGenres());
        existingMovie.setGenres(savedGenres);
    }

    /// Updates the countries of the existing movie with the provided incoming country data.
    ///
    /// @param existingMovie The existing Movie entity to update.
    /// @param incomingMovie The Movie entity containing new country information.
    private void updateCountries(Movie existingMovie, Movie incomingMovie) {
        if (incomingMovie.getCountries() == null || incomingMovie.getCountries().isEmpty()) {
            return;
        }

        log.debug("updating countries");

        List<Country> savedCountries = metadataService.getSavedCountries(incomingMovie.getCountries());
        existingMovie.setCountries(savedCountries);
    }

    /// Retrieves a paginated list of movies that belong to the specified genre.
    ///
    /// @param genreId The UUID of the genre to filter movies by.
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @return A Page containing movies that match the specified genre.
    public Page<Movie> getMoviesWithGenre(UUID genreId, Integer page, Integer limit) {
        log.info("retrieving movies with genre: {}, page: {}, limit: {}", genreId, page, limit);

        metadataService.getGenre(genreId);
        return searchService.getMoviesWithGenre(genreId, page, limit);
    }

    /// Retrieves a paginated list of movies that belong to the specified country.
    ///
    /// @param countryId The UUID of the country to filter movies by.
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @return A Page containing movies that match the specified country.
    public Page<Movie> getMoviesWithCountry(UUID countryId, Integer page, Integer limit) {
        log.info("retrieving movies with country: {}, page: {}, limit: {}", countryId, page, limit);

        metadataService.getCountry(countryId);
        return searchService.getMoviesWithCountry(countryId, page, limit);
    }

    /// Retrieves a paginated list of movies that belong to the specified production company.
    ///
    /// @param companyId The UUID of the production company to filter movies by.
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @return A Page containing movies that match the specified production company.
    public Page<Movie> getMoviesWithProductionCompany(UUID companyId, Integer page, Integer limit) {
        log.info("retrieving movies with production company: {}, page: {}, limit: {}", companyId, page, limit);

        metadataService.getProductionCompany(companyId);
        return searchService.getMoviesWithProductionCompany(companyId, page, limit);
    }

    /// Retrieves a paginated list of movies that belong to the specified director.
    ///
    /// @param directorId The UUID of the director to filter movies by.
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @return A Page containing movies that match the specified director.
    public Page<Movie> getMoviesWithDirector(UUID directorId, Integer page, Integer limit) {
        log.info("retrieving movies with director: {}, page: {}, limit: {}", directorId, page, limit);

        crewService.getDirector(directorId);
        return searchService.getMoviesWithDirector(directorId, page, limit);
    }

    /// Retrieves a paginated list of movies that belong to the specified actor.
    ///
    /// @param actorId The UUID of the actor to filter movies by.
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @return A Page containing movies that match the specified actor.
    public Page<Movie> getMoviesWithActor(UUID actorId, Integer page, Integer limit) {
        log.info("retrieving movies with actor: {}, page: {}, limit: {}", actorId, page, limit);

        crewService.getActor(actorId);
        return searchService.getMoviesWithActor(actorId, page, limit);
    }

    /// Adds a comment to a movie specified by its unique identifier (UUID).
    ///
    /// @param movieId The UUID of the movie to which the comment will be added.
    /// @param text The text of the comment to be added.
    public void addComment(UUID movieId, String text) {
        Movie movie = getMovie(movieId);

        interactionService.addComment(movie, text);
    }

    /// Retrieves a paginated list of comments for a specific movie.
    ///
    /// @param movieId The UUID of the movie for which comments are to be retrieved.
    /// @param page The page number for pagination.
    /// @param limit The maximum number of comments per page.
    /// @param sort The sorting criteria for the comments.
    /// @return A Page containing comments for the specified movie.
    /// @throws MovieNotFoundException if the movie with the given ID is not found.
    public Page<Comment> getComments(UUID movieId, Integer page, Integer limit, String sort) {
        getMovie(movieId);
        return interactionService.getComments(movieId, page, limit, sort);
    }

    /// Adds a rating for a movie specified by its unique identifier (UUID).
    ///
    /// This method updates the movie's rating and review count. If the rating is new,
    /// the review count is incremented. The average rating is recalculated after adding
    /// the new rating.
    ///
    /// @param movieId The UUID of the movie to which the rating will be added.
    /// @param rating The rating value to be added (between 0 and 10).
    /// @throws MovieNotFoundException if the movie with the given ID is not found.
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

    /// Retrieves the rating for a movie specified by its unique identifier (UUID).
    ///
    /// @param movieId The UUID of the movie for which the rating is to be retrieved.
    /// @return The average rating of the specified movie.
    /// @throws MovieNotFoundException if the movie with the given ID is not found.
    public Double getRating(UUID movieId) {
        log.info("retrieving rating for movie: {}", movieId);

        Movie movie = getMovie(movieId);
        return interactionService.getRating(movie);
    }

    /// Retrieves a paginated list of movies based on the specified pagination and sorting parameters.
    ///
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @param sort The sorting criteria for the movies.
    /// @return A Page containing a list of movies.
    public Page<Movie> getMovies(Integer page, Integer limit, String sort) {
        log.info("retrieving movies with page: {}, limit: {}, sort: {}", page, limit, sort);
        return searchService.getMovies(page, limit, sort);
    }

    /// Filters movies based on the specified criteria, including release year, genre, and country.
    ///
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @param sort The sorting criteria for the movies.
    /// @param releaseYear The release year to filter by.
    /// @param genre The genre to filter by.
    /// @param country The country to filter by.
    /// @return A Page containing a filtered list of movies.
    public Page<Movie> filterMovies(Integer page, Integer limit, String sort, String releaseYear, String genre, String country) {
        log.info("filtering movies with page: {}, limit: {}, sort: {}, releaseYear: {}, genre: {}, country: {}", page, limit, sort, releaseYear, genre, country);
        return searchService.filterMovies(page, limit, sort, releaseYear, genre, country);
    }

    /// Searches for movies based on a keyword and retrieves a paginated list of results.
    ///
    /// @param page The page number for pagination.
    /// @param limit The maximum number of movies per page.
    /// @param sort The sorting criteria for the movies.
    /// @param keyword The keyword to search for in movie titles or descriptions.
    /// @return A Page containing a list of movies that match the search criteria.
    public Page<Movie> searchMovies(Integer page, Integer limit, String sort, String keyword) {
        log.info("searching movies with page: {}, limit: {}, sort: {}, keyword: {}", page, limit, sort, keyword);
        return searchService.searchMovies(page, limit, sort, keyword);
    }

    /// Retrieves a list of distinct release years for movies in the repository.
    ///
    /// @return A List containing distinct release years.
    public List<Integer> getYears() {
        return movieRepository.findDistinctReleaseYears();
    }

}
