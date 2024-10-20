package com.moviehub.mapper.entity;

import com.moviehub.entity.Movie_;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MovieFieldMapper {

    private MovieFieldMapper() {
    }

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
