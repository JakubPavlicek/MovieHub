package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing movie crew members, including directors and actors.
/// This class provides methods for retrieving saved directors and movie casts,
/// as well as deleting movie casts associated with a movie.
@Service
@Transactional
@RequiredArgsConstructor
public class MovieCrewService {

    /// Service for managing Director entities.
    private final DirectorService directorService;
    /// Service for managing MovieCast entities.
    private final MovieCastService castService;
    /// Service for managing Actor entities.
    private final ActorService actorService;

    /// Retrieves a saved director based on the provided Director entity.
    ///
    /// @param director The Director entity to retrieve.
    /// @return The saved Director entity.
    public Director getSavedDirector(Director director) {
        return directorService.getSavedDirector(director);
    }

    /// Retrieves saved movie casts for a given movie.
    ///
    /// @param cast A list of MovieCast entities to check and save.
    /// @param savedMovie The Movie entity associated with the MovieCasts.
    /// @return A list of saved MovieCast entities.
    public List<MovieCast> getSavedMovieCasts(List<MovieCast> cast, Movie savedMovie) {
        return castService.getSavedMovieCasts(cast, savedMovie);
    }

    /// Deletes all movie casts associated with the specified movie.
    ///
    /// @param existingMovie The Movie entity whose associated MovieCasts should be deleted.
    public void deleteAllMovieCastsByMovie(Movie existingMovie) {
        castService.deleteAllMovieCastsByMovie(existingMovie);
    }

    /// Retrieves a director by their unique identifier.
    ///
    /// @param directorId The UUID of the Director to retrieve.
    /// @return The Director entity with the specified ID.
    public Director getDirector(UUID directorId) {
        return directorService.getDirector(directorId);
    }

    /// Retrieves an actor by their unique identifier.
    ///
    /// @param actorId The UUID of the Actor to retrieve.
    /// @return The Actor entity with the specified ID.
    public Actor getActor(UUID actorId) {
        return actorService.getActor(actorId);
    }

}
