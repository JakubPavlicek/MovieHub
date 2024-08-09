package com.movie_manager.exception;

public class ActorNotFoundException extends RuntimeException {

    public ActorNotFoundException(String message) {
        super(message);
    }
    
}
