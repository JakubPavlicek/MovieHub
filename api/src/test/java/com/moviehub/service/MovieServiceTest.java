package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createActor;
import static com.moviehub.EntityBuilder.createComment;
import static com.moviehub.EntityBuilder.createCountry;
import static com.moviehub.EntityBuilder.createDirector;
import static com.moviehub.EntityBuilder.createGenre;
import static com.moviehub.EntityBuilder.createIncomingMovieWithEmptyLists;
import static com.moviehub.EntityBuilder.createMovie;
import static com.moviehub.EntityBuilder.createProductionCompany;
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
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getMovie(movieId))
            .isInstanceOf(MovieNotFoundException.class);
    }

    @Test
    void shouldGetMovieWithDetails() {
        UUID movieId = movie.getId();

        when(movieRepository.existsById(movieId)).thenReturn(true);
        when(movieRepository.getMovieWithDirector(movieId)).thenReturn(movie);
        when(movieRepository.getMovieWithGenres(movie)).thenReturn(movie);
        when(movieRepository.getMovieWithProduction(movie)).thenReturn(movie);
        when(movieRepository.getMovieWithCastAndActors(movie)).thenReturn(movie);
        when(movieRepository.getMovieWithCountries(movie)).thenReturn(movie);

        Movie foundMovie = movieService.getMovieWithDetails(movieId);

        assertThat(foundMovie).isNotNull();
        assertThat(foundMovie.getName()).isEqualTo(movie.getName());
        assertThat(foundMovie.getDirector()).isEqualTo(movie.getDirector());
        assertThat(foundMovie.getCountries()).isEqualTo(movie.getCountries());
        assertThat(foundMovie.getGenres()).isEqualTo(movie.getGenres());
        assertThat(foundMovie.getCast()).isEqualTo(movie.getCast());
        assertThat(foundMovie.getProduction()).isEqualTo(movie.getProduction());
    }

    @Test
    void shouldThrowMovieNotFoundExceptionWhenMovieWithDetailsDoesNotExist() {
        UUID movieId = movie.getId();

        when(movieRepository.existsById(movieId)).thenReturn(false);

        assertThatThrownBy(() -> movieService.getMovieWithDetails(movieId))
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
        Genre genre = createGenre("Action");
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(metadataService.getGenre(any())).thenReturn(genre);
        when(searchService.getMoviesWithGenre(genre.getId(), PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithGenre(genre.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithCountry() {
        Country country = createCountry("USA");
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(metadataService.getCountry(any())).thenReturn(country);
        when(searchService.getMoviesWithCountry(country.getId(), PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithCountry(country.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithProductionCompany() {
        ProductionCompany productionCompany = createProductionCompany("A24");
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(metadataService.getProductionCompany(any())).thenReturn(productionCompany);
        when(searchService.getMoviesWithProductionCompany(productionCompany.getId(), PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithProductionCompany(productionCompany.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithDirector() {
        Director director = createDirector("James");
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(crewService.getDirector(any())).thenReturn(director);
        when(searchService.getMoviesWithDirector(director.getId(), PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithDirector(director.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldGetMoviesWithActor() {
        Actor actor = createActor("John");
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(crewService.getActor(any())).thenReturn(actor);
        when(searchService.getMoviesWithActor(actor.getId(), PAGE, LIMIT)).thenReturn(moviePage);

        Page<Movie> result = movieService.getMoviesWithActor(actor.getId(), PAGE, LIMIT);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(movie);
    }

    @Test
    void shouldAddComment() {
        UUID movieId = movie.getId();
        Comment comment = createComment();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        movieService.addComment(movieId, comment.getText());

        verify(interactionService).addComment(movie, comment.getText());
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

}