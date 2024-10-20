package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a reply is not found in the system.
public class ReplyNotFoundException extends RuntimeException {

    public ReplyNotFoundException(String message) {
        super(message);
    }

}
