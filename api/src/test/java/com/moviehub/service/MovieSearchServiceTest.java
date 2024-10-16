package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.entity.Movie_;
import com.moviehub.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieSearchServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ParseService parseService;

    @InjectMocks
    private MovieSearchService movieSearchService;

    private static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.by("updatedAt").descending());
    private static final String NAME = Movie_.NAME;
    private static final List<Movie> MOVIES = Collections.singletonList(new Movie());
    private static final UUID DEFAULT_ID = UUID.fromString("5ef66573-c4f9-483f-b02c-3fbe9f22a84d");
    private static final UUID LIST_ID = UUID.fromString("a3cd828e-9b2e-403a-9728-86fb52545328");

    @Test
    void shouldReturnMoviesWithGenre() {
        Page<UUID> movieIdsPage = new PageImpl<>(List.of(LIST_ID), PAGEABLE, 1);

        when(movieRepository.findMovieIdsByGenreId(DEFAULT_ID, PAGEABLE)).thenReturn(movieIdsPage);
        when(movieRepository.findMoviesWithGenresByIds(movieIdsPage.getContent())).thenReturn(MOVIES);

        Page<Movie> moviePage = movieSearchService.getMoviesWithGenre(DEFAULT_ID, 0, 10);

        verify(movieRepository).findMovieIdsByGenreId(DEFAULT_ID, PAGEABLE);
        verify(movieRepository).findMoviesWithGenresByIds(movieIdsPage.getContent());
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithCountry() {
        Page<UUID> movieIdsPage = new PageImpl<>(List.of(LIST_ID), PAGEABLE, 1);

        when(movieRepository.findMovieIdsByCountryId(DEFAULT_ID, PAGEABLE)).thenReturn(movieIdsPage);
        when(movieRepository.findMoviesWithGenresByIds(movieIdsPage.getContent())).thenReturn(MOVIES);

        Page<Movie> moviePage = movieSearchService.getMoviesWithCountry(DEFAULT_ID, 0, 10);

        verify(movieRepository).findMovieIdsByCountryId(DEFAULT_ID, PAGEABLE);
        verify(movieRepository).findMoviesWithGenresByIds(movieIdsPage.getContent());
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithProductionCompany() {
        Page<UUID> movieIdsPage = new PageImpl<>(List.of(LIST_ID), PAGEABLE, 1);

        when(movieRepository.findMovieIdsByCompanyId(DEFAULT_ID, PAGEABLE)).thenReturn(movieIdsPage);
        when(movieRepository.findMoviesWithGenresByIds(movieIdsPage.getContent())).thenReturn(MOVIES);

        Page<Movie> moviePage = movieSearchService.getMoviesWithProductionCompany(DEFAULT_ID, 0, 10);

        verify(movieRepository).findMovieIdsByCompanyId(DEFAULT_ID, PAGEABLE);
        verify(movieRepository).findMoviesWithGenresByIds(movieIdsPage.getContent());
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithDirector() {
        Page<UUID> movieIdsPage = new PageImpl<>(List.of(LIST_ID), PAGEABLE, 1);

        when(movieRepository.findMovieIdsByDirectorId(DEFAULT_ID, PAGEABLE)).thenReturn(movieIdsPage);
        when(movieRepository.findMoviesWithGenresByIds(movieIdsPage.getContent())).thenReturn(MOVIES);

        Page<Movie> moviePage = movieSearchService.getMoviesWithDirector(DEFAULT_ID, 0, 10);

        verify(movieRepository).findMovieIdsByDirectorId(DEFAULT_ID, PAGEABLE);
        verify(movieRepository).findMoviesWithGenresByIds(movieIdsPage.getContent());
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithActor() {
        Page<UUID> movieIdsPage = new PageImpl<>(List.of(LIST_ID), PAGEABLE, 1);

        when(movieRepository.findMovieIdsByActorId(DEFAULT_ID, PAGEABLE)).thenReturn(movieIdsPage);
        when(movieRepository.findMoviesWithGenresByIds(movieIdsPage.getContent())).thenReturn(MOVIES);

        Page<Movie> moviePage = movieSearchService.getMoviesWithActor(DEFAULT_ID, 0, 10);

        verify(movieRepository).findMovieIdsByActorId(DEFAULT_ID, PAGEABLE);
        verify(movieRepository).findMoviesWithGenresByIds(movieIdsPage.getContent());
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldGetMovies() {
        when(parseService.parseMovieSort(any(String.class))).thenReturn(Sort.by(NAME).ascending());
        when(movieRepository.getMoviePreviews(any(Pageable.class))).thenReturn(new PageImpl<>(MOVIES, PAGEABLE, 1));

        Page<Movie> moviePage = movieSearchService.getMovies(0, 10, NAME);

        verify(parseService).parseMovieSort(NAME);
        verify(movieRepository).getMoviePreviews(any(Pageable.class));
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldFilterMovies() {
        when(parseService.parseMovieSort(any(String.class))).thenReturn(Sort.by(NAME).ascending());
        when(movieRepository.getMoviePreviews(any(), any(Pageable.class))).thenReturn(new PageImpl<>(MOVIES, PAGEABLE, 1));

        Page<Movie> moviePage = movieSearchService.filterMovies(0, 10, NAME, "2022", "Action", "USA");

        verify(parseService).parseMovieSort(NAME);
        verify(movieRepository).getMoviePreviews(any(), any(Pageable.class));
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldSearchMovies() {
        when(parseService.parseMovieSort(any(String.class))).thenReturn(Sort.by(NAME).ascending());
        when(movieRepository.getMoviePreviews(any(), any(Pageable.class))).thenReturn(new PageImpl<>(MOVIES, PAGEABLE, 1));

        Page<Movie> moviePage = movieSearchService.searchMovies(0, 10, NAME, "keyword");

        verify(parseService).parseMovieSort(NAME);
        verify(movieRepository).getMoviePreviews(any(), any(Pageable.class));
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

}