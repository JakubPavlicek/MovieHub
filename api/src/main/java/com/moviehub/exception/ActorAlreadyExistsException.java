package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when trying to add an actor that already exists in the system.
public class ActorAlreadyExistsException extends RuntimeException {

    public ActorAlreadyExistsException(String message) {
        super(message);
    }

}
