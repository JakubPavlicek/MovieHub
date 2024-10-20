package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.repository.MovieCastRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing movie casts.
/// This class provides methods for adding, retrieving, and deleting movie cast entities.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MovieCastService {

    /// Repository for managing MovieCast entities.
    private final MovieCastRepository movieCastRepository;

    /// Service for managing Actor entities.
    private final ActorService actorService;

    /// Retrieves saved movie casts by checking each MovieCast in the provided list.
    ///
    /// @param movieCasts A list of MovieCast entities to check.
    /// @param movie The Movie entity associated with the movie casts.
    /// @return A list of saved MovieCast entities found in the database.
    public List<MovieCast> getSavedMovieCasts(List<MovieCast> movieCasts, Movie movie) {
        log.info("retrieving saved movie casts");

        return movieCasts.stream()
                         .map(movieCast -> getSavedMovieCast(movieCast, movie))
                         .collect(Collectors.toCollection(ArrayList::new));
    }

    /// Retrieves a saved movie cast by getting the saved Actor and associating it with the given Movie.
    ///
    /// @param movieCast The MovieCast entity to check and save.
    /// @param movie The Movie entity associated with the MovieCast.
    /// @return The saved MovieCast entity.
    private MovieCast getSavedMovieCast(MovieCast movieCast, Movie movie) {
        Actor savedActor = actorService.getSavedActor(movieCast.getActor());
        movieCast.setActor(savedActor);
        movieCast.setMovie(movie);

        return movieCastRepository.save(movieCast);
    }

    /// Deletes all movie casts associated with a specific movie.
    ///
    /// @param movie The Movie entity whose associated MovieCasts should be deleted.
    public void deleteAllMovieCastsByMovie(Movie movie) {
        movieCastRepository.deleteAllByMovie(movie);
    }

}
