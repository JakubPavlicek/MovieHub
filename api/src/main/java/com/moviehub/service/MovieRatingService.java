package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.entity.User;
import com.moviehub.repository.MovieRatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing movie ratings. This class handles saving,
/// updating, and retrieving ratings for movies by users.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MovieRatingService {

    /// Repository for handling movie ratings in the database.
    private final MovieRatingRepository ratingRepository;

    /// Service for managing user-related operations.
    private final UserService userService;

    /// Saves a rating for a specified movie. If the user has already rated the movie,
    /// the existing rating will be updated. Otherwise, a new rating will be created.
    ///
    /// @param movie The Movie entity for which the rating is being saved.
    /// @param rating The rating value to be saved.
    /// @return true if the rating was updated, false if a new rating was created.
    public boolean saveRating(Movie movie, Double rating) {
        log.info("saving rating: {} for movie: {}", rating, movie.getId());

        User user = userService.getUser();
        Optional<MovieRating> existingMovieRating = ratingRepository.findByMovieAndUser(movie, user);

        // user has already rated the movie -> update the rating
        if (existingMovieRating.isPresent()) {
            updateExistingRating(existingMovieRating.get(), rating);
            return true;
        }

        createNewRating(movie, user, rating);
        return false;
    }

    /// Updates an existing rating with a new rating value.
    ///
    /// @param existingRating The existing MovieRating entity to be updated.
    /// @param rating The new rating value.
    private void updateExistingRating(MovieRating existingRating, Double rating) {
        log.debug("updating existing rating: {} to new rating: {}", existingRating.getRating(), rating);

        existingRating.setRating(rating);
        ratingRepository.save(existingRating);
    }

    /// Creates a new rating for a specified movie and user.
    ///
    /// @param movie The Movie entity for which the rating is being created.
    /// @param user The User entity who is rating the movie.
    /// @param rating The rating value to be saved.
    private void createNewRating(Movie movie, User user, Double rating) {
        log.info("creating new rating: {} for movie: {}", rating, movie.getId());

        MovieRating movieRating = MovieRating.builder()
                                             .movie(movie)
                                             .user(user)
                                             .rating(rating)
                                             .build();

        ratingRepository.save(movieRating);
    }

    /// Calculates the average rating for a specified movie.
    ///
    /// @param movieId The UUID of the Movie for which the average rating is to be calculated.
    /// @return The average rating for the specified movie.
    public Double calculateRating(UUID movieId) {
        log.debug("calculating average rating for movie: {}", movieId);

        return ratingRepository.getAverageRatingByMovieId(movieId);
    }

    /// Retrieves the rating for a specified movie from the current user.
    ///
    /// @param movie The Movie entity for which the rating is being retrieved.
    /// @return The rating value, or 0 if the user has not rated the movie.
    public Double getRating(Movie movie) {
        Optional<MovieRating> existingMovieRating = ratingRepository.findByMovieAndUser(movie, userService.getUser());

        Double rating = existingMovieRating.map(MovieRating::getRating)
                                           .orElse(0d);

        log.info("retrieved rating: {}", rating);

        return rating;
    }

}
