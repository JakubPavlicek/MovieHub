package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a country is not found in the system.
public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException(String message) {
        super(message);
    }

}
