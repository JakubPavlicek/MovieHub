package com.movie_manager.exception;

public class CountryAlreadyExistsException extends RuntimeException {

    public CountryAlreadyExistsException(String message) {
        super(message);
    }

}
