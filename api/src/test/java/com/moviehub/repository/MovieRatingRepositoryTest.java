package com.moviehub.repository;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.moviehub.EntityBuilder.createDirector;
import static com.moviehub.EntityBuilder.createMovie;
import static com.moviehub.EntityBuilder.createMovieRating;
import static com.moviehub.EntityBuilder.createUser;
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
    private TestEntityManager entityManager;

    private static Movie movie;

    @BeforeEach
    void setUp() {
        movie = createMovie("Movie");

        Director director = entityManager.persistAndFlush(createDirector("Director"));
        movie.setDirector(director);

        movie = entityManager.persistAndFlush(movie);
    }

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldGetAverageRating() {
        User user1 = entityManager.persistAndFlush(createUser("Test User"));
        User user2 = entityManager.persistAndFlush(createUser("Another User"));

        entityManager.persistAndFlush(createMovieRating(movie, user1, 4.0));
        entityManager.persistAndFlush(createMovieRating(movie, user2, 5.0));

        Double averageRating = movieRatingRepository.getAverageRatingByMovieId(movie.getId());

        assertThat(averageRating).isEqualTo(4.5);
    }

    @Test
    void shouldReturnNullRatingWhenMovieHasNoRating() {
        Double averageRating = movieRatingRepository.getAverageRatingByMovieId(movie.getId());

        assertThat(averageRating).isNull();
    }

}