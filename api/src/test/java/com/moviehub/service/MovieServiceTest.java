package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.exception.MovieNotFoundException;
import com.moviehub.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieSearchService searchService;

    @Mock
    private MovieCrewService crewService;

    @Mock
    private MovieMetadataService metadataService;

    @Mock
    private MovieInteractionService interactionService;

    @InjectMocks
    private MovieService movieService;

    private static final Integer PAGE = 0;
    private static final Integer LIMIT = 10;
    private static final String SORT = "name";
    private static final String RELEASE_YEAR = "2024";
    private static final String GENRE = "Action";
    private static final String COUNTRY = "USA";
    private static final String KEYWORD = "keyword";
    private static final String UPDATED_MOVIE_NAME = "Updated Movie";

    private static final UUID[] CAST_UUIDS = {
        UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
        UUID.fromString("a76b9465-98d4-4b02-8f29-e5855e5cfc54"),
        UUID.fromString("0e582ab1-4c90-4c30-93a5-29f575f77efb")
    };

    private static final UUID[] GENRE_UUIDS = {
        UUID.fromString("8d6f3bfa-93de-41f0-a654-3a93735e0d68"),
        UUID.fromString("14ac6b61-8057-4be5-8b2c-d6b7b4f501c3")
    };

    private static final UUID[] COUNTRY_UUIDS = {
        UUID.fromString("e6720e43-b689-4f76-8cf6-3d474d8c9d39"),
        UUID.fromString("50f3e074-ff36-4db3-b4c4-ecacb82951bb")
    };

    private static final UUID[] PRODUCTION_COMPANY_UUIDS = {
        UUID.fromString("e45d9e2d-fb94-4e45-8985-d88d32c0544f"),
        UUID.fromString("0b5a2b8c-f98e-49e3-bd5b-377a22b9e093")
    };

    private Movie movie;
    private Movie incomingMovie;

    @BeforeEach
    void setUp() {
        movie = createMovie();

        incomingMovie = new Movie();
    }

    @Test
    void shouldSaveMovieWithRelatedEntitiesWhenAddingMovie() {
        when(crewService.getSavedDirector(any())).thenReturn(movie.getDirector());
        when(metadataService.getSavedCountries(anyList())).thenReturn(movie.getCountries());
        when(metadataService.getSavedGenres(anyList())).thenReturn(movie.getGenres());
        when(metadataService.getSavedProductions(anyList())).thenReturn(movie.getProduction());
        when(crewService.getSavedMovieCasts(anyList(), any())).thenReturn(movie.getCast());
        when(movieRepository.save(any())).thenReturn(movie);

        Movie savedMovie = movieService.addMovie(movie);

        verify(movieRepository).save(movie);
        assertThat(savedMovie).isNotNull();
        assertThat(savedMovie.getName()).isEqualTo(movie.getName());
        assertThat(savedMovie.getDirector()).isEqualTo(movie.getDirector());
        assertThat(savedMovie.getCountries()).isEqualTo(movie.getCountries());
        assertThat(savedMovie.getGenres()).isEqualTo(movie.getGenres());
        assertThat(savedMovie.getCast()).isEqualTo(movie.getCast());
        assertThat(savedMovie.getProduction()).isEqualTo(movie.getProduction());
    }

    @Test
    void shouldReturnMovieWhenMovieExists() {
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Movie foundMovie = movieService.getMovie(movieId);

        assertThat(foundMovie).isEqualTo(movie);
    }

    @Test
    void shouldThrowMovieNotFoundExceptionWhenMovieDoesNotExist() {
        UUID movieId = UUID.randomUUID();

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getMovie(movieId))
            .isInstanceOf(MovieNotFoundException.class);
    }

    @Test
    void shouldRemoveMovieWhenMovieExists() {
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(movieId);

        verify(movieRepository).delete(movie);
    }

    @Test
    void shouldUpdateMovie() {
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any())).thenReturn(movie);

        incomingMovie.setName(UPDATED_MOVIE_NAME);

        Movie updatedMovie = movieService.updateMovie(movieId, incomingMovie);

        assertThat(updatedMovie.getName()).isEqualTo(UPDATED_MOVIE_NAME);
        verify(movieRepository).save(movie);
    }

    @Test
    void shouldUpdateMovieWithNonNullIncomingMovie() {
        Movie oldMovie = new Movie();

        when(movieRepository.findById(any())).thenReturn(Optional.of(oldMovie));
        when(movieRepository.save(any())).thenReturn(movie);

        Movie updatedMovie = movieService.updateMovie(oldMovie.getId(), movie);

        assertThat(updatedMovie.getName()).isEqualTo(movie.getName());
        assertThat(updatedMovie.getReleaseDate()).isEqualTo(movie.getReleaseDate());
        assertThat(updatedMovie.getDuration()).isEqualTo(movie.getDuration());
        assertThat(updatedMovie.getDescription()).isEqualTo(movie.getDescription());
        assertThat(updatedMovie.getPosterUrl()).isEqualTo(movie.getPosterUrl());
        assertThat(updatedMovie.getTrailerUrl()).isEqualTo(movie.getTrailerUrl());
        assertThat(updatedMovie.getDirector()).isEqualTo(movie.getDirector());
        assertThat(updatedMovie.getCast()).isEqualTo(movie.getCast());
        assertThat(updatedMovie.getProduction()).isEqualTo(movie.getProduction());
        assertThat(updatedMovie.getCountries()).isEqualTo(movie.getCountries());
        assertThat(updatedMovie.getGenres()).isEqualTo(movie.getGenres());
    }

    @Test
    void shouldUpdateMovieWithEmptyListsInIncomingMovie() {
        Movie oldMovie = new Movie();
        incomingMovie = createIncomingMovieWithEmptyLists();

        when(movieRepository.findById(any())).thenReturn(Optional.of(oldMovie));
        when(movieRepository.save(any())).thenReturn(incomingMovie);

        Movie updatedMovie = movieService.updateMovie(oldMovie.getId(), incomingMovie);

        assertThat(updatedMovie.getName()).isEqualTo(incomingMovie.getName());
        assertThat(updatedMovie.getReleaseDate()).isEqualTo(incomingMovie.getReleaseDate());
        assertThat(updatedMovie.getDuration()).isEqualTo(incomingMovie.getDuration());
        assertThat(updatedMovie.getDescription()).isEqualTo(incomingMovie.getDescription());
        assertThat(updatedMovie.getPosterUrl()).isEqualTo(incomingMovie.getPosterUrl());
        assertThat(updatedMovie.getTrailerUrl()).isEqualTo(incomingMovie.getTrailerUrl());
        assertThat(updatedMovie.getDirector()).isEqualTo(incomingMovie.getDirector());
        assertThat(updatedMovie.getCast()).isEmpty();
        assertThat(updatedMovie.getProduction()).isEmpty();
        assertThat(updatedMovie.getCountries()).isEmpty();
        assertThat(updatedMovie.getGenres()).isEmpty();
    }

    @Test
    void shouldListMovies() {
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(searchService.getMovies(PAGE, LIMIT, SORT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMovies(PAGE, LIMIT, SORT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldFilterMovies() {
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(searchService.filterMovies(PAGE, LIMIT, SORT, RELEASE_YEAR, GENRE, COUNTRY)).thenReturn(moviePage);

        Page<Movie> result = movieService.filterMovies(PAGE, LIMIT, SORT, RELEASE_YEAR, GENRE, COUNTRY);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldSearchMovies() {
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(searchService.searchMovies(PAGE, LIMIT, SORT, KEYWORD)).thenReturn(moviePage);

        Page<Movie> result = movieService.searchMovies(PAGE, LIMIT, SORT, KEYWORD);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithGenre() {
        Genre genre = createGenre();
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(metadataService.getGenre(any())).thenReturn(genre);
        when(searchService.getMoviesWithGenre(genre, PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithGenre(genre.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithCountry() {
        Country country = createCountry();
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(metadataService.getCountry(any())).thenReturn(country);
        when(searchService.getMoviesWithCountry(country, PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithCountry(country.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithProductionCompany() {
        ProductionCompany productionCompany = createProductionCompany();
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(metadataService.getProductionCompany(any())).thenReturn(productionCompany);
        when(searchService.getMoviesWithProductionCompany(productionCompany, PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithProductionCompany(productionCompany.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithDirector() {
        Director director = createDirector();
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(crewService.getDirector(any())).thenReturn(director);
        when(searchService.getMoviesWithDirector(director, PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithDirector(director.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithActor() {
        Actor actor = createActor();
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(crewService.getActor(any())).thenReturn(actor);
        when(searchService.getMoviesWithActor(actor, PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithActor(actor.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldAddComment() {
        UUID movieId = movie.getId();
        Comment comment = createComment();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        movieService.addComment(movieId, comment, null);

        verify(interactionService).saveComment(movie, comment, null);
    }

    @Test
    void shouldGetComments() {
        UUID movieId = movie.getId();
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(createComment()));

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(interactionService.getComments(any(), anyInt(), anyInt(), anyString())).thenReturn(commentPage);

        Page<Comment> result = movieService.getComments(movieId, PAGE, LIMIT, SORT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldAddRating() {
        UUID movieId = movie.getId();
        double rating = 4.5;

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(interactionService.saveRating(any(), anyDouble())).thenReturn(false);
        when(interactionService.calculateRating(movieId)).thenReturn(4.5);
        when(movieRepository.save(any())).thenReturn(movie);

        movieService.addRating(movieId, rating);

        verify(interactionService).saveRating(movie, rating);
    }

    @Test
    void shouldIncreaseReviewCountWhenAddingRatingToMovie() {
        UUID movieId = movie.getId();
        double rating = 4.5;

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(interactionService.saveRating(any(), anyDouble())).thenReturn(true);
        when(interactionService.calculateRating(movieId)).thenReturn(4.5);
        when(movieRepository.save(any())).thenReturn(movie);

        movieService.addRating(movieId, rating);

        verify(interactionService).saveRating(movie, rating);
    }

    @Test
    void shouldThrowMovieNotFoundExceptionWhenAddingRatingForNonExistentMovie() {
        UUID movieId = UUID.randomUUID();
        double rating = 4.5;

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.addRating(movieId, rating))
            .isInstanceOf(MovieNotFoundException.class);
    }

    @Test
    void shouldGetRating() {
        Double rating = 5.0;

        when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(interactionService.getRating(movie)).thenReturn(rating);

        Double actualRating = movieService.getRating(movie.getId());

        assertThat(actualRating).isEqualTo(rating);
    }

    @Test
    void shouldGetYears() {
        when(movieRepository.findDistinctReleaseYears()).thenReturn(List.of(2024, 2025));

        movieService.getYears();

        verify(movieRepository).findDistinctReleaseYears();
    }

    private static Movie createMovie() {
        return Movie.builder()
                    .id(UUID.fromString("24eadcf9-353c-4d12-82a3-441cbf177897"))
                    .name("Movie")
                    .releaseDate(LocalDate.of(2023, 1, 1))
                    .duration(120)
                    .description("A test movie.")
                    .posterUrl("https://example.com/poster.jpg")
                    .trailerUrl("https://example.com/trailer.mp4")
                    .director(createDirector())
                    .cast(createMovieCasts())
                    .production(createProductionCompanies())
                    .countries(createCountries())
                    .genres(createGenres())
                    .build();
    }

    private static Movie createIncomingMovieWithEmptyLists() {
        return Movie.builder()
                    .id(UUID.fromString("29bcc401-925b-4770-93fb-bbeb9173b8d6"))
                    .name("New Movie")
                    .releaseDate(LocalDate.of(2024, 1, 1))
                    .duration(150)
                    .description("A new movie.")
                    .posterUrl("https://example.com/posterImage.jpg")
                    .trailerUrl("https://example.com/trailerVideo.mp4")
                    .director(createDirector())
                    .cast(Collections.emptyList())
                    .production(Collections.emptyList())
                    .countries(Collections.emptyList())
                    .genres(Collections.emptyList())
                    .build();
    }

    private static Comment createComment() {
        return Comment.builder()
                      .id(UUID.fromString("71765262-d96f-4e9f-b909-657ec0b837ee"))
                      .text("This is a test comment.")
                      .build();
    }

    private static Director createDirector() {
        return Director.builder()
                       .id(UUID.fromString("ba65bf44-4b8c-4348-885a-83c5e1ee36ae"))
                       .name("James")
                       .build();
    }

    private static Genre createGenre() {
        return Genre.builder()
                    .id(UUID.fromString("26d56677-fffa-4e1c-9ab6-267c2b19cfca"))
                    .build();
    }

    private static Country createCountry() {
        return Country.builder()
                      .id(UUID.fromString("fa3e3ea9-816f-472d-8cd4-ee10f5452389"))
                      .build();
    }

    private static ProductionCompany createProductionCompany() {
        return ProductionCompany.builder()
                                .id(UUID.fromString("c772576e-6501-42ae-b518-d84dbb5690f6"))
                                .build();
    }

    private static Actor createActor() {
        return Actor.builder()
                    .id(UUID.fromString("c750ed3b-84e5-43f8-9899-619970954845"))
                    .build();
    }

    private static List<MovieCast> createMovieCasts() {
        List<MovieCast> casts = new ArrayList<>();
        for (UUID castUuid : CAST_UUIDS) {
            MovieCast cast = new MovieCast();
            cast.setId(castUuid);
            cast.setActor(new Actor());
            casts.add(cast);
        }
        return casts;
    }

    private static List<Genre> createGenres() {
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < GENRE_UUIDS.length; i++) {
            Genre genre = new Genre();
            genre.setId(GENRE_UUIDS[i]);
            genre.setName("Genre " + (i + 1));
            genres.add(genre);
        }
        return genres;
    }

    private static List<Country> createCountries() {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < COUNTRY_UUIDS.length; i++) {
            Country country = new Country();
            country.setId(COUNTRY_UUIDS[i]);
            country.setName("Country " + (i + 1));
            countries.add(country);
        }
        return countries;
    }

    private static List<ProductionCompany> createProductionCompanies() {
        List<ProductionCompany> companies = new ArrayList<>();
        for (int i = 0; i < PRODUCTION_COMPANY_UUIDS.length; i++) {
            ProductionCompany company = new ProductionCompany();
            company.setId(PRODUCTION_COMPANY_UUIDS[i]);
            company.setName("Company " + (i + 1));
            companies.add(company);
        }
        return companies;
    }

}