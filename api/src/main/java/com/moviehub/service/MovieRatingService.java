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

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MovieRatingService {

    private final MovieRatingRepository ratingRepository;

    private final UserService userService;

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

    private void updateExistingRating(MovieRating existingRating, Double rating) {
        log.debug("updating existing rating: {} to new rating: {}", existingRating.getRating(), rating);

        existingRating.setRating(rating);
        ratingRepository.save(existingRating);
    }

    private void createNewRating(Movie movie, User user, Double rating) {
        log.info("creating new rating: {} for movie: {}", rating, movie.getId());

        MovieRating movieRating = MovieRating.builder()
                                             .movie(movie)
                                             .user(user)
                                             .rating(rating)
                                             .build();

        ratingRepository.save(movieRating);
    }

    public Double calculateRating(UUID movieId) {
        log.debug("calculating average rating for movie: {}", movieId);

        return ratingRepository.getAverageRatingByMovieId(movieId);
    }

    public Double getRating(Movie movie) {
        Optional<MovieRating> existingMovieRating = ratingRepository.findByMovieAndUser(movie, userService.getUser());

        Double rating = existingMovieRating.map(MovieRating::getRating).orElse(0d);

        log.info("retrieved rating: {}", rating);

        return rating;
    }

}
