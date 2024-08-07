package com.movie_manager.exception;

public class DirectorNotFoundException extends RuntimeException {

    public DirectorNotFoundException(String message) {
        super(message);
    }

}
