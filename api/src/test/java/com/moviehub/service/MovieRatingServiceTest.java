package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.entity.User;
import com.moviehub.repository.MovieRatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRatingServiceTest {

    @Mock
    private MovieRatingRepository ratingRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MovieRatingService movieRatingService;

    private static final UUID MOVIE_ID = UUID.fromString("946290a7-4e09-4d07-8202-0cca8c8ab197");
    private static final Double RATING = 8.0;

    @Test
    void shouldSaveNewRating() {
        User user = createUser();
        Movie movie = createMovie();

        when(userService.getUser()).thenReturn(user);
        when(ratingRepository.findByMovieAndUser(movie, user)).thenReturn(Optional.empty());

        boolean result = movieRatingService.saveRating(movie, RATING);

        assertThat(result).isFalse();
        verify(ratingRepository, times(1)).save(any(MovieRating.class));
    }

    @Test
    void shouldUpdateExistingRating() {
        User user = createUser();
        Movie movie = createMovie();
        MovieRating existingRating = createMovieRating(5.0, movie, user);

        when(userService.getUser()).thenReturn(user);
        when(ratingRepository.findByMovieAndUser(movie, user)).thenReturn(Optional.of(existingRating));

        boolean result = movieRatingService.saveRating(movie, RATING);

        assertThat(result).isTrue();
        assertThat(existingRating.getRating()).isEqualTo(RATING);
        verify(ratingRepository, times(1)).save(existingRating);
    }

    @Test
    void shouldReturnExistingRating() {
        User user = createUser();
        Movie movie = createMovie();
        MovieRating existingRating = createMovieRating(RATING, movie, user);

        when(userService.getUser()).thenReturn(user);
        when(ratingRepository.findByMovieAndUser(movie, user)).thenReturn(Optional.of(existingRating));

        Double rating = movieRatingService.getRating(movie);

        assertThat(rating).isEqualTo(RATING);
    }

    @Test
    void shouldReturnZeroRatingWhenNotRated() {
        User user = createUser();
        Movie movie = createMovie();

        when(userService.getUser()).thenReturn(user);
        when(ratingRepository.findByMovieAndUser(movie, user)).thenReturn(Optional.empty());

        Double rating = movieRatingService.getRating(movie);

        assertThat(rating).isZero();
    }

    @Test
    void shouldCalculateAverageRating() {
        when(ratingRepository.getAverageRatingByMovieId(MOVIE_ID)).thenReturn(RATING);

        Double averageRating = movieRatingService.calculateRating(MOVIE_ID);

        assertThat(averageRating).isEqualTo(RATING);
    }

    private static Movie createMovie() {
        return Movie.builder()
                    .id(MOVIE_ID)
                    .build();
    }

    private static User createUser() {
        return User.builder()
                   .id("auth0|1")
                   .build();
    }

    private static MovieRating createMovieRating(Double rating, Movie movie, User user) {
        return MovieRating.builder()
                          .movie(movie)
                          .user(user)
                          .rating(rating)
                          .build();
    }

}