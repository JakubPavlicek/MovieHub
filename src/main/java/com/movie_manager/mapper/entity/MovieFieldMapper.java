package com.movie_manager.mapper.entity;

import com.movie_manager.entity.Movie_;

public class MovieFieldMapper {

    private MovieFieldMapper() {
    }

    public enum Field {
        MOVIE_ID,
        NAME,
        RELEASE_DATE,
        LENGTH,
        DESCRIPTION,
        DIRECTOR,
        ACTORS,
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
            case LENGTH -> Movie_.LENGTH;
            case DESCRIPTION -> Movie_.DESCRIPTION;
            case DIRECTOR -> Movie_.DIRECTOR;
            case ACTORS -> Movie_.ACTORS;
            case GENRES -> Movie_.GENRES;
            case COUNTRIES -> Movie_.COUNTRIES;
        };
    }

}
