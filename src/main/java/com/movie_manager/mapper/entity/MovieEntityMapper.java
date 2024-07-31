package com.movie_manager.mapper.entity;

import com.movie_manager.entity.Movie_;

public class MovieEntityMapper {

    private MovieEntityMapper() {
    }

    public enum Field {
        MOVIE_ID,
        NAME,
        RELEASE,
        LENGTH,
        DESCRIPTION,
        DIRECTOR,
        ACTORS,
        GENRES,
        COUNTRIES,
    }

    public static String mapToMovieField(String field) {
        Field movieField = Field.valueOf(field.toUpperCase());

        return switch (movieField) {
            case MOVIE_ID -> Movie_.MOVIE_ID;
            case NAME -> Movie_.NAME;
            case RELEASE -> Movie_.RELEASE;
            case LENGTH -> Movie_.LENGTH;
            case DESCRIPTION -> Movie_.DESCRIPTION;
            case DIRECTOR -> Movie_.DIRECTOR;
            case ACTORS -> Movie_.ACTORS;
            case GENRES -> Movie_.GENRES;
            case COUNTRIES -> Movie_.COUNTRIES;
        };
    }

}
