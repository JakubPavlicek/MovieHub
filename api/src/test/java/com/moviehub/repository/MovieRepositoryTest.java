package com.moviehub.repository;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.entity.Actor;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {"spring.liquibase.contexts=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class MovieRepositoryTest {

    @Autowired
    private PostgreSQLContainer<?> postgres;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieCastRepository movieCastRepository;

    private static final Pageable pageable = PageRequest.ofSize(10);

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllMoviesWhenActorIsValid() {
        Movie movie = createMovie("Movie", LocalDate.parse("2024-10-13"));
        Actor actor = createActor();
        createMovieCast(movie, actor);

        Page<Movie> movies = movieRepository.findMovieIdsByActorId(actor, pageable);

        assertThat(movies.getContent()).hasSize(1);
        assertThat(movies.getContent()
                         .getFirst()
                         .getName()).isEqualTo("Movie");
    }

    @Test
    void shouldReturnEmptyMoviesWhenActorIsInvalid() {
        Page<Movie> movies = movieRepository.findMovieIdsByActorId(null, pageable);

        assertThat(movies.getContent()).isEmpty();
    }

    @Test
    void shouldFindDistinctReleaseYears() {
        createMovieWithReleaseYear(2024);
        createMovieWithReleaseYear(2024);
        createMovieWithReleaseYear(2019);

        List<Integer> distinctReleaseYears = movieRepository.findDistinctReleaseYears();

        assertThat(distinctReleaseYears).hasSize(2)
                                        .containsExactly(2024, 2019);
    }

    private Movie createMovie(String name, LocalDate releaseDate) {
        return movieRepository.save(Movie.builder()
                                         .name(name)
                                         .filename("movie.mp4")
                                         .releaseDate(releaseDate)
                                         .duration(120)
                                         .description("A test movie")
                                         .posterUrl("https://example.com/poster.jpg")
                                         .trailerUrl("https://example.com/trailer.mp4")
                                         .build());
    }

    private void createMovieWithReleaseYear(int year) {
        createMovie("Movie" + year, LocalDate.of(year, 10, 13));
    }

    private Actor createActor() {
        return actorRepository.save(Actor.builder()
                                         .name("Test Actor")
                                         .build());
    }

    private void createMovieCast(Movie movie, Actor actor) {
        movieCastRepository.save(MovieCast.builder()
                                          .movie(movie)
                                          .actor(actor)
                                          .role("tester")
                                          .build());
    }

}
