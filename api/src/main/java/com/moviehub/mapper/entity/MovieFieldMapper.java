package com.moviehub.mapper.entity;

import com.moviehub.entity.Movie_;
import lombok.extern.log4j.Log4j2;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for mapping movie fields to their corresponding database entity fields.
@Log4j2
public class MovieFieldMapper {

    // Private constructor to prevent instantiation.
    private MovieFieldMapper() {
    }

    /// Enumeration of the fields related to movies.
    public enum Field {
        MOVIE_ID,
        NAME,
        FILENAME,
        RELEASE_DATE,
        DURATION,
        DESCRIPTION,
        RATING,
        REVIEW_COUNT,
        POSTER_URL,
        TRAILER_URL,
        DIRECTOR,
        COMMENTS,
        CAST,
        PRODUCTION,
        GENRES,
        COUNTRIES,
        RATINGS,
        UPDATED_AT
    }

    /// Maps a given movie field string to the corresponding entity field.
    ///
    /// @param field The name of the movie field to map.
    /// @return The corresponding entity field name as a string.
    public static String mapToMovieField(String field) {
        log.debug("mapping movie field: {}", field);
        // replaceAll: insert "_" before every uppercased letter
        Field movieField = Field.valueOf(field.replaceAll("([A-Z])", "_$1").toUpperCase());

        return switch (movieField) {
            case MOVIE_ID -> Movie_.ID;
            case NAME -> Movie_.NAME;
            case FILENAME -> Movie_.FILENAME;
            case RELEASE_DATE -> Movie_.RELEASE_DATE;
            case DURATION -> Movie_.DURATION;
            case DESCRIPTION -> Movie_.DESCRIPTION;
            case RATING -> Movie_.RATING;
            case REVIEW_COUNT -> Movie_.REVIEW_COUNT;
            case POSTER_URL -> Movie_.POSTER_URL;
            case TRAILER_URL -> Movie_.TRAILER_URL;
            case DIRECTOR -> Movie_.DIRECTOR;
            case COMMENTS -> Movie_.COMMENTS;
            case CAST -> Movie_.CAST;
            case PRODUCTION -> Movie_.PRODUCTION;
            case GENRES -> Movie_.GENRES;
            case COUNTRIES -> Movie_.COUNTRIES;
            case RATINGS -> Movie_.RATINGS;
            case UPDATED_AT -> Movie_.UPDATED_AT;
        };
    }

}
