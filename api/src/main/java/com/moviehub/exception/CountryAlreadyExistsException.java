package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a country already exists in the system.
public class CountryAlreadyExistsException extends RuntimeException {

    public CountryAlreadyExistsException(String message) {
        super(message);
    }

}
