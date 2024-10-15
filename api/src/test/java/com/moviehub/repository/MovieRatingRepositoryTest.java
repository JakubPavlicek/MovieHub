package com.moviehub.repository;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {"spring.liquibase.contexts=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class MovieRatingRepositoryTest {

    @Autowired
    private PostgreSQLContainer<?> postgres;

    @Autowired
    private MovieRatingRepository movieRatingRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldGetAverageRating() {
        Movie movie = createMovie();
        User user = createUser("Test User");

        createMovieRating(movie, user, 4.0);
        createMovieRating(movie, createUser("Another User"), 5.0);

        Double averageRating = movieRatingRepository.getAverageRatingByMovieId(movie.getId());

        assertThat(averageRating).isEqualTo(4.5);
    }

    @Test
    void shouldReturnNullRatingWhenMovieHasNoRating() {
        Movie movie = createMovie();

        Double averageRating = movieRatingRepository.getAverageRatingByMovieId(movie.getId());

        assertThat(averageRating).isNull();
    }

    private Movie createMovie() {
        return movieRepository.save(Movie.builder()
                                         .name("Movie")
                                         .filename("movie.mp4")
                                         .releaseDate(LocalDate.parse("2024-10-13"))
                                         .duration(120)
                                         .description("A test movie")
                                         .posterUrl("https://example.com/poster.jpg")
                                         .trailerUrl("https://example.com/trailer.mp4")
                                         .director(createDirector())
                                         .build());
    }

    private Director createDirector() {
        return directorRepository.save(Director.builder()
                                               .name("Director")
                                               .build());
    }

    private User createUser(String name) {
        return userRepository.save(User.builder()
                                       .id("auth0|" + name)
                                       .name(name)
                                       .email("test@example.com")
                                       .pictureUrl("https://example.com/image.jpg")
                                       .build());
    }

    private void createMovieRating(Movie movie, User user, double rating) {
        movieRatingRepository.save(MovieRating.builder()
                                              .movie(movie)
                                              .user(user)
                                              .rating(rating)
                                              .build());
    }

}