package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a production company already exists in the system.
public class ProductionCompanyAlreadyExistsException extends RuntimeException {

    public ProductionCompanyAlreadyExistsException(String message) {
        super(message);
    }

}
