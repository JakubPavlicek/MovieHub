package com.moviehub.mapper.entity;

import com.moviehub.entity.Movie_;

public class MovieFieldMapper {

    private MovieFieldMapper() {
    }

    public enum Field {
        MOVIE_ID,
        NAME,
        RELEASE_DATE,
        DURATION,
        DESCRIPTION,
        RATING,
        REVIEW_COUNT,
        POSTER_URL,
        TRAILER_URL,
        DIRECTOR,
        CAST,
        PRODUCTION,
        GENRES,
        COUNTRIES,
    }

    public static String mapToMovieField(String field) {
        // replaceAll: insert "_" before every uppercased letter
        Field movieField = Field.valueOf(field.replaceAll("([A-Z])", "_$1").toUpperCase());

        return switch (movieField) {
            case MOVIE_ID -> Movie_.MOVIE_ID;
            case NAME -> Movie_.NAME;
            case RELEASE_DATE -> Movie_.RELEASE_DATE;
            case DURATION -> Movie_.DURATION;
            case DESCRIPTION -> Movie_.DESCRIPTION;
            case RATING -> Movie_.RATING;
            case REVIEW_COUNT -> Movie_.REVIEW_COUNT;
            case POSTER_URL -> Movie_.POSTER_URL;
            case TRAILER_URL -> Movie_.TRAILER_URL;
            case DIRECTOR -> Movie_.DIRECTOR;
            case CAST -> Movie_.CAST;
            case PRODUCTION -> Movie_.PRODUCTION;
            case GENRES -> Movie_.GENRES;
            case COUNTRIES -> Movie_.COUNTRIES;
        };
    }

}
