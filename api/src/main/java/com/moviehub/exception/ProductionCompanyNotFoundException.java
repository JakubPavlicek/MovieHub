package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a production company is not found in the system.
public class ProductionCompanyNotFoundException extends RuntimeException {

    public ProductionCompanyNotFoundException(String message) {
        super(message);
    }

}
