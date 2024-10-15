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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private DirectorRepository directorRepository;

    @Autowired
    private MovieCastRepository castRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ProductionCompanyRepository companyRepository;

    @Autowired
    private CountryRepository countryRepository;

    private static final Pageable PAGEABLE = PageRequest.ofSize(10);
    private static final String MOVIE_NAME = "Movie";
    private static final String FILENAME = "test.mp4";
    private static final Integer DURATION = 120;
    private static final String POSTER_URL = "https://example.com/poster.jpg";
    private static final String TRAILER_URL = "https://example.com/trailer.mp4";
    private static final String DESCRIPTION = "A test movie";

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindMoviesByDirectorId() {
        Movie movie = createMovie();

        Page<UUID> movieIds = movieRepository.findMovieIdsByDirectorId(movie.getDirector().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByCompanyId() {
        Movie movie = createMovie();

        Page<UUID> movieIds = movieRepository.findMovieIdsByCompanyId(movie.getProduction().getFirst().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByGenreId() {
        Movie movie = createMovie();

        Page<UUID> movieIds = movieRepository.findMovieIdsByGenreId(movie.getGenres().getFirst().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByCountryId() {
        Movie movie = createMovie();

        Page<UUID> movieIds = movieRepository.findMovieIdsByCountryId(movie.getCountries().getFirst().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesByActorId() {
        Movie movie = createMovie();

        Page<UUID> movieIds = movieRepository.findMovieIdsByActorId(movie.getCast().getFirst().getActor().getId(), PAGEABLE);

        assertThat(movieIds.getContent()).hasSize(1);
        assertThat(movieIds.getContent().getFirst()).isEqualTo(movie.getId());
    }

    @Test
    void shouldFindMoviesWithGenresByIds() {
        Movie movie = createMovie();

        List<Movie> movies = movieRepository.findMoviesWithGenresByIds(List.of(movie.getId()));

        assertThat(movies).hasSize(1);
        assertThat(movies.getFirst().getGenres()).contains(movie.getGenres().getFirst());
    }

    @Test
    void shouldFindDistinctReleaseYears() {
        createMovieWithReleaseYear(2024, "Director 1");
        createMovieWithReleaseYear(2024, "Director 2");
        createMovieWithReleaseYear(2022, "Director 3");

        List<Integer> years = movieRepository.findDistinctReleaseYears();

        assertThat(years).containsExactly(2024, 2022);
    }

    @Test
    void shouldGetMovieWithDirector() {
        Movie movie = createMovie();

        Movie result = movieRepository.getMovieWithDirector(movie.getId());

        assertThat(result.getDirector()).isEqualTo(movie.getDirector());
    }

    @Test
    void shouldGetMovieWithCastAndActors() {
        Movie movie = createMovie();

        Movie result = movieRepository.getMovieWithCastAndActors(movie);

        assertThat(result.getCast()).hasSize(1);
        assertThat(result.getCast().getFirst().getActor()).isEqualTo(movie.getCast().getFirst().getActor());
    }

    @Test
    void shouldGetMovieWithProduction() {
        Movie movie = createMovie();

        Movie result = movieRepository.getMovieWithProduction(movie);

        assertThat(result.getProduction()).isEqualTo(movie.getProduction());
    }

    @Test
    void shouldGetMovieWithGenres() {
        Movie movie = createMovie();

        Movie result = movieRepository.getMovieWithGenres(movie);

        assertThat(result.getGenres()).contains(movie.getGenres().getFirst());
    }

    @Test
    void shouldGetMovieWithCountries() {
        Movie movie = createMovie();

        Movie result = movieRepository.getMovieWithCountries(movie);

        assertThat(result.getCountries()).contains(movie.getCountries().getFirst());
    }

    @Test
    void shouldGetMoviePreviews() {
        Movie movie = createMovie();

        Page<Movie> result = movieRepository.getMoviePreviews(PAGEABLE);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getGenres()).contains(movie.getGenres().getFirst());
    }

    @Test
    void shouldGetMoviePreviewsBySpecification() {
        Movie movie = createMovie();

        Specification<Movie> specification = MovieSpecification.releaseYearIn(List.of(2024));
        Page<Movie> result = movieRepository.getMoviePreviews(specification, PAGEABLE);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getGenres()).contains(movie.getGenres().getFirst());
    }

    private Director createDirector(String name) {
        return directorRepository.save(Director.builder().name(name).build());
    }

    private Genre createGenre() {
        return genreRepository.save(Genre.builder().name("Action").build());
    }

    private Actor createActor() {
        return actorRepository.save(Actor.builder().name("James").build());
    }

    private ProductionCompany createCompany() {
        return companyRepository.save(ProductionCompany.builder().name("A24").build());
    }

    private Country createCountry() {
        return countryRepository.save(Country.builder().name("USA").build());
    }

    private MovieCast createMovieCast(Movie movie, Actor actor) {
        return castRepository.save(MovieCast.builder().movie(movie).actor(actor).role("role").build());
    }

    private Movie createMovie() {
        Movie movie = Movie.builder()
                           .name(MOVIE_NAME)
                           .filename(FILENAME)
                           .releaseDate(LocalDate.of(2024, 1, 1))
                           .duration(DURATION)
                           .description(DESCRIPTION)
                           .posterUrl(POSTER_URL)
                           .trailerUrl(TRAILER_URL)
                           .director(createDirector("Director"))
                           .countries(new ArrayList<>(List.of(createCountry())))
                           .genres(new ArrayList<>(List.of(createGenre())))
                           .production(new ArrayList<>(List.of(createCompany())))
                           .build();

        movie = movieRepository.save(movie);
        movie.setCast(new ArrayList<>(List.of(createMovieCast(movie, createActor()))));

        return movieRepository.save(movie);
    }

    private void createMovieWithReleaseYear(int year, String directorName) {
        movieRepository.save(Movie.builder()
                                  .name(MOVIE_NAME + " " + year)
                                  .filename(FILENAME)
                                  .releaseDate(LocalDate.of(year, 1, 1))
                                  .duration(DURATION)
                                  .description(DESCRIPTION)
                                  .posterUrl(POSTER_URL)
                                  .trailerUrl(TRAILER_URL)
                                  .director(createDirector(directorName))
                                  .build());
    }

}
