package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.repository.MovieRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieRatingService {

    private final MovieRatingRepository ratingRepository;

    public MovieRating saveRating(Movie movie, Double rating) {
        ensureUniqueRating(movie, "7d63632f-792a-4691-b7d3-7d9096afba30");

        MovieRating movieRating = MovieRating.builder()
                                             .movie(movie)
                                             .userId("7d63632f-792a-4691-b7d3-7d9096afba30")
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
