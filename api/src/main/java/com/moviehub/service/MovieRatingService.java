package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.repository.MovieRatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MovieRatingService {

    private final MovieRatingRepository ratingRepository;

    private final UserService userService;

    public boolean saveRating(Movie movie, Double rating) {
        log.info("Saving rating {} for movie {}", rating, movie.getId());

        Optional<MovieRating> existingMovieRating = ratingRepository.findByMovieAndUser(movie, userService.getUser());

        // user has already rated the movie -> update the rating
        if (existingMovieRating.isPresent()) {
            updateExistingRating(existingMovieRating.get(), rating);
            return true;
        }

        createNewRating(movie, rating);

        return false;
    }

    private void updateExistingRating(MovieRating existingRating, Double rating) {
        existingRating.setRating(rating);
        ratingRepository.save(existingRating);
    }

    private void createNewRating(Movie movie, Double rating) {
        MovieRating movieRating = MovieRating.builder()
                                             .movie(movie)
                                             .user(userService.getUser())
                                             .rating(rating)
                                             .build();

        ratingRepository.save(movieRating);
    }

    public Double calculateRating(UUID movieId) {
        return ratingRepository.getAverageRatingByMovieId(movieId);
    }

    public Double getRating(Movie movie) {
        Optional<MovieRating> existingMovieRating = ratingRepository.findByMovieAndUser(movie, userService.getUser());

        if (existingMovieRating.isEmpty()) {
            log.info("returning '0' rating");
            return 0d;
        }

        log.info("returning rating '{}'", existingMovieRating.get().getRating());

        return existingMovieRating.get().getRating();
    }

}
