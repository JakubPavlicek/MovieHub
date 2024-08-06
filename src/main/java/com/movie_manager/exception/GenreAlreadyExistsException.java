package com.movie_manager.exception;

public class GenreAlreadyExistsException extends RuntimeException {

    public GenreAlreadyExistsException(String message) {
        super(message);
    }

}
