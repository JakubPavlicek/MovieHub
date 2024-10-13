package com.moviehub.mapper.entity;

import com.moviehub.entity.Movie_;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MovieFieldMapperTest {

    private static Stream<Arguments> movieFieldNames() {
        return Stream.of(
            Arguments.of(MovieFieldMapper.Field.MOVIE_ID, Movie_.ID),
            Arguments.of(MovieFieldMapper.Field.NAME, Movie_.NAME),
            Arguments.of(MovieFieldMapper.Field.FILENAME, Movie_.FILENAME),
            Arguments.of(MovieFieldMapper.Field.RELEASE_DATE, Movie_.RELEASE_DATE),
            Arguments.of(MovieFieldMapper.Field.DURATION, Movie_.DURATION),
            Arguments.of(MovieFieldMapper.Field.DESCRIPTION, Movie_.DESCRIPTION),
            Arguments.of(MovieFieldMapper.Field.RATING, Movie_.RATING),
            Arguments.of(MovieFieldMapper.Field.REVIEW_COUNT, Movie_.REVIEW_COUNT),
            Arguments.of(MovieFieldMapper.Field.POSTER_URL, Movie_.POSTER_URL),
            Arguments.of(MovieFieldMapper.Field.TRAILER_URL, Movie_.TRAILER_URL),
            Arguments.of(MovieFieldMapper.Field.DIRECTOR, Movie_.DIRECTOR),
            Arguments.of(MovieFieldMapper.Field.COMMENTS, Movie_.COMMENTS),
            Arguments.of(MovieFieldMapper.Field.CAST, Movie_.CAST),
            Arguments.of(MovieFieldMapper.Field.PRODUCTION, Movie_.PRODUCTION),
            Arguments.of(MovieFieldMapper.Field.GENRES, Movie_.GENRES),
            Arguments.of(MovieFieldMapper.Field.COUNTRIES, Movie_.COUNTRIES),
            Arguments.of(MovieFieldMapper.Field.RATINGS, Movie_.RATINGS),
            Arguments.of(MovieFieldMapper.Field.UPDATED_AT, Movie_.UPDATED_AT)
        );
    }

    private static Stream<Arguments> movieFieldNamesWithoutUnderscore() {
        return Stream.of(
            Arguments.of("movieId", Movie_.ID),
            Arguments.of("releaseDate", Movie_.RELEASE_DATE),
            Arguments.of("reviewCount", Movie_.REVIEW_COUNT),
            Arguments.of("posterUrl", Movie_.POSTER_URL),
            Arguments.of("trailerUrl", Movie_.TRAILER_URL),
            Arguments.of("updatedAt", Movie_.UPDATED_AT)
        );
    }

    @ParameterizedTest
    @MethodSource("movieFieldNames")
    void shouldReturnCorrectMovieField(MovieFieldMapper.Field field, String expectedFieldName) {
        String movieFieldName = MovieFieldMapper.mapToMovieField(field.name().toLowerCase());
        assertThat(movieFieldName).isEqualTo(expectedFieldName);
    }

    @ParameterizedTest
    @MethodSource("movieFieldNamesWithoutUnderscore")
    void shouldReturnCorrectMovieFieldWhenFieldIsCamelCase(String field, String expectedFieldName) {
        String movieFieldName = MovieFieldMapper.mapToMovieField(field);
        assertThat(movieFieldName).isEqualTo(expectedFieldName);
    }

    @ParameterizedTest
    @MethodSource("movieFieldNames")
    void shouldNotReturnMovieFieldWhenFieldIsUppercase(MovieFieldMapper.Field field) {
        assertThatIllegalArgumentException().isThrownBy(() -> MovieFieldMapper.mapToMovieField(field.name()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDocumentFieldIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() -> MovieFieldMapper.mapToMovieField("null"));
    }

}