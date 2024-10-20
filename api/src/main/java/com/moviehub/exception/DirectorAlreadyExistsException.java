package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a director already exists in the system.
public class DirectorAlreadyExistsException extends RuntimeException {

    public DirectorAlreadyExistsException(String message) {
        super(message);
    }

}
