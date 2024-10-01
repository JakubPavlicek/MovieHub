package com.moviehub.exception;

public class MovieRatingNotFoundException extends RuntimeException {

    public MovieRatingNotFoundException(String message) {
        super(message);
    }

}
