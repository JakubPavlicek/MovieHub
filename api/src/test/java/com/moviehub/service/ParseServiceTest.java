package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.specification.MovieSpecification;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ParseServiceTest {

    @InjectMocks
    private ParseService parseService;

    @ParameterizedTest
    @ValueSource(strings = {
        "name.asc",
        "releaseDate.desc",
        "duration.asc",
        "name.asc,releaseDate.desc,duration.asc"
    })
    void shouldParseValidMovieSortSuccessfully(String sort) {
        Sort parsedSort = parseService.parseMovieSort(sort);

        assertThat(parsedSort).isNotNull();

        parsedSort.forEach(order -> {
            assertThat(order.getDirection()).isIn(Sort.Direction.ASC, Sort.Direction.DESC);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "createdAt.asc",
        "createdAt.desc"
    })
    void shouldParseValidCommentSortSuccessfully(String sort) {
        Sort parsedSort = parseService.parseCommentSort(sort);

        assertThat(parsedSort).isNotNull();

        Sort.Order order = parsedSort.getOrderFor("createdAt");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isIn(Sort.Direction.ASC, Sort.Direction.DESC);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2020", "2021", "2020,2021,2022"
    })
    void shouldParseReleaseYearSuccessfully(String releaseYear) {
        Specification<Movie> specification = parseService.parseReleaseYear(releaseYear);

        assertThat(specification).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Action", "Drama", "Action,Drama"
    })
    void shouldParseGenreSuccessfully(String genre) {
        Specification<Movie> specification = parseService.parseGenre(genre);

        assertThat(specification).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "USA", "Canada", "USA,Canada"
    })
    void shouldParseCountrySuccessfully(String country) {
        Specification<Movie> specification = parseService.parseCountry(country);

        assertThat(specification).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"all"})
    void shouldReturnConjunctionForAllGenres(String genre) {
        Specification<Movie> specification = parseService.parseGenre(genre);

        assertThat(specification).isNotNull().isEqualTo(MovieSpecification.conjunction());
    }

    @ParameterizedTest
    @ValueSource(strings = {"all"})
    void shouldReturnConjunctionForAllCountries(String country) {
        Specification<Movie> specification = parseService.parseCountry(country);

        assertThat(specification).isNotNull().isEqualTo(MovieSpecification.conjunction());
    }

}