package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.Movie_;
import com.moviehub.entity.ProductionCompany;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.by(Movie_.UPDATED_AT).descending());
    private static final Sort NAME_SORT = Sort.by(Movie_.NAME).ascending();
    private static final Page<Movie> MOVIE_PAGE = new PageImpl<>(Collections.singletonList(createMovie()));

    @Test
    void shouldReturnMoviesWithGenre() {
        Genre genre = createGenre();
        when(movieRepository.findAllByGenresContaining(genre, PAGEABLE)).thenReturn(MOVIE_PAGE);

        Page<Movie> moviePage = movieSearchService.getMoviesWithGenre(genre, 0, 10);

        verify(movieRepository).findAllByGenresContaining(genre, PAGEABLE);
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithCountry() {
        Country country = createCountry();
        when(movieRepository.findAllByCountriesContaining(country, PAGEABLE)).thenReturn(MOVIE_PAGE);

        Page<Movie> moviePage = movieSearchService.getMoviesWithCountry(country, 0, 10);

        verify(movieRepository).findAllByCountriesContaining(country, PAGEABLE);
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithProductionCompany() {
        ProductionCompany productionCompany = createProductionCompany();
        when(movieRepository.findMovieIdsByCompanyId(productionCompany, PAGEABLE)).thenReturn(MOVIE_PAGE);

        Page<Movie> moviePage = movieSearchService.getMoviesWithProductionCompany(productionCompany, 0, 10);

        verify(movieRepository).findMovieIdsByCompanyId(productionCompany, PAGEABLE);
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithDirector() {
        Director director = createDirector();
        when(movieRepository.findMovieIdsByDirectorId(director, PAGEABLE)).thenReturn(MOVIE_PAGE);

        Page<Movie> moviePage = movieSearchService.getMoviesWithDirector(director, 0, 10);

        verify(movieRepository).findMovieIdsByDirectorId(director, PAGEABLE);
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldReturnMoviesWithActor() {
        Actor actor = createActor();
        when(movieRepository.findMovieIdsByActorId(actor, PAGEABLE)).thenReturn(MOVIE_PAGE);

        Page<Movie> moviePage = movieSearchService.getMoviesWithActor(actor, 0, 10);

        verify(movieRepository).findMovieIdsByActorId(actor, PAGEABLE);
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    @Test
    void shouldGetMovies() {
        when(parseService.parseMovieSort(anyString())).thenReturn(NAME_SORT);
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(MOVIE_PAGE);

        Page<Movie> moviePage = movieSearchService.getMovies(0, 10, Movie_.NAME);

        verify(parseService).parseMovieSort(Movie_.NAME);
        assertThat(moviePage).isNotNull();
        assertThat(moviePage.getContent()).hasSize(1);
    }

    private static Movie createMovie() {
        return Movie.builder()
                    .name("Movie")
                    .build();
    }

    private static Genre createGenre() {
        return Genre.builder()
                    .name("Action")
                    .build();
    }

    private static Country createCountry() {
        return Country.builder()
                      .name("USA")
                      .build();
    }

    private static Director createDirector() {
        return Director.builder()
                       .name("James")
                       .build();
    }

    private static Actor createActor() {
        return Actor.builder()
                    .name("Arnold")
                    .build();
    }

    private static ProductionCompany createProductionCompany() {
        return ProductionCompany.builder()
                                .name("Universal Pictures")
                                .build();
    }

}