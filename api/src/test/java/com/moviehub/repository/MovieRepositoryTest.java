package com.moviehub.repository;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.entity.Actor;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.specification.MovieSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createActor;
import static com.moviehub.EntityBuilder.createCountry;
import static com.moviehub.EntityBuilder.createDirector;
import static com.moviehub.EntityBuilder.createGenre;
import static com.moviehub.EntityBuilder.createMovie;
import static com.moviehub.EntityBuilder.createMovieCast;
import static com.moviehub.EntityBuilder.createMovieWithYear;
import static com.moviehub.EntityBuilder.createProductionCompany;
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
    private TestEntityManager entityManager;

    private static final Pageable PAGEABLE = PageRequest.ofSize(10);

    private static Movie movie;

    @BeforeEach
    void setUp() {
        movie = createMovie("Movie");

        Director director = entityManager.persistAndFlush(createDirector("James Wan"));

        movie.setDirector(director);
        movie = movieRepository.saveAndFlush(movie);

        Genre action = entityManager.persistAndFlush(createGenre("Action"));
        Genre thriller = entityManager.persistAndFlush(createGenre("Thriller"));

        Country usa = entityManager.persistAndFlush(createCountry("USA"));
        Country japan = entityManager.persistAndFlush(createCountry("Japan"));

        ProductionCompany a24 = entityManager.persistAndFlush(createProductionCompany("A24"));
        ProductionCompany syncopy = entityManager.persistAndFlush(createProductionCompany("Syncopy"));

        Actor arnold = entityManager.persistAndFlush(createActor("Arnold"));
        Actor john = entityManager.persistAndFlush(createActor("John"));

        MovieCast arnoldCast = entityManager.persistAndFlush(createMovieCast(movie, arnold));
        MovieCast johnCast = entityManager.persistAndFlush(createMovieCast(movie, john));

        movie.setGenres(new ArrayList<>(Arrays.asList(action, thriller)));
        movie.setCountries(new ArrayList<>(Arrays.asList(usa, japan)));
        movie.setProduction(new ArrayList<>(Arrays.asList(a24, syncopy)));
        movie.setCast(new ArrayList<>(Arrays.asList(arnoldCast, johnCast)));

        movie = movieRepository.saveAndFlush(movie);
    }

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindMoviesByDirectorId() {
        Page<UUID> movieIds = movieRepository.findMovieIdsByDirectorId(movie.getDirector().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByCompanyId() {
        Page<UUID> movieIds = movieRepository.findMovieIdsByCompanyId(movie.getProduction().getFirst().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByGenreId() {
        Page<UUID> movieIds = movieRepository.findMovieIdsByGenreId(movie.getGenres().getFirst().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByCountryId() {
        Page<UUID> movieIds = movieRepository.findMovieIdsByCountryId(movie.getCountries().getFirst().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByActorId() {
        Page<UUID> movieIds = movieRepository.findMovieIdsByActorId(movie.getCast().getFirst().getActor().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesWithGenresByIds() {
        List<Movie> movies = movieRepository.findMoviesWithGenresByIds(List.of(movie.getId()));

        assertThat(movies).hasSize(1);
        assertThat(movies.getFirst().getGenres()).contains(movie.getGenres().getFirst());
    }

    @Test
    void shouldFindDistinctReleaseYears() {
        createMovieWithYearAndDirector("Movie 1", 2024, "Director 1");
        createMovieWithYearAndDirector("Movie 2", 2024, "Director 2");
        createMovieWithYearAndDirector("Movie 3", 2022, "Director 3");

        List<Integer> years = movieRepository.findDistinctReleaseYears();

        assertThat(years).containsExactly(2024, 2022);
    }

    @Test
    void shouldGetMovieWithDirector() {
        Movie result = movieRepository.getMovieWithDirector(movie.getId());

        assertThat(result.getDirector()).isEqualTo(movie.getDirector());
    }

    @Test
    void shouldGetMovieWithCastAndActors() {
        Movie result = movieRepository.getMovieWithCastAndActors(movie);

        assertThat(result.getCast()).hasSize(2);
        assertThat(result.getCast().getFirst().getActor()).isEqualTo(movie.getCast().getFirst().getActor());
        assertThat(result.getCast().get(1).getActor()).isEqualTo(movie.getCast().get(1).getActor());
    }

    @Test
    void shouldGetMovieWithProduction() {
        Movie result = movieRepository.getMovieWithProduction(movie);

        assertThat(result.getProduction()).isEqualTo(movie.getProduction());
    }

    @Test
    void shouldGetMovieWithGenres() {
        Movie result = movieRepository.getMovieWithGenres(movie);

        assertThat(result.getGenres()).contains(movie.getGenres().getFirst());
    }

    @Test
    void shouldGetMovieWithCountries() {
        Movie result = movieRepository.getMovieWithCountries(movie);

        assertThat(result.getCountries()).contains(movie.getCountries().getFirst());
    }

    @Test
    void shouldGetMoviePreviews() {
        Page<Movie> result = movieRepository.getMoviePreviews(PAGEABLE);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getGenres()).contains(movie.getGenres().getFirst());
    }

    @Test
    void shouldGetMoviePreviewsBySpecification() {
        Specification<Movie> specification = MovieSpecification.releaseYearIn(List.of(2024));
        Page<Movie> result = movieRepository.getMoviePreviews(specification, PAGEABLE);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getGenres()).contains(movie.getGenres().getFirst());
    }

    private void createMovieWithYearAndDirector(String name, int year, String directorName) {
        Director director = createDirector(directorName);
        director = entityManager.persistAndFlush(director);

        Movie movieWithYear = createMovieWithYear(name, year);
        movieWithYear.setDirector(director);

        movieRepository.saveAndFlush(movieWithYear);
    }

}
