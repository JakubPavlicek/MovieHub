package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.repository.MovieRatingRepository;
import com.moviehub.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieRatingService {

    private final MovieRatingRepository ratingRepository;

    public MovieRating saveRating(Movie movie, Double rating) {
        ensureUniqueRating(movie, AuthUser.getUserId());

        MovieRating movieRating = MovieRating.builder()
                                             .movie(movie)
                                             .userId(AuthUser.getUserId())
                                             .rating(rating)
                                             .build();

        return ratingRepository.save(movieRating);
    }

    private void ensureUniqueRating(Movie movie, String userId) {
        // user has already rated the movie -> remove the rating
        if (ratingRepository.existsByMovieAndUserId(movie, userId)) {
            ratingRepository.removeByMovieAndUserId(movie, userId);
        }
    }

    public Double calculateRating(String movieId) {
        return ratingRepository.getAverageRatingByMovieId(movieId);
    }

}
