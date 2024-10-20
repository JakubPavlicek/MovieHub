package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a genre already exists in the system.
public class GenreAlreadyExistsException extends RuntimeException {

    public GenreAlreadyExistsException(String message) {
        super(message);
    }

}
