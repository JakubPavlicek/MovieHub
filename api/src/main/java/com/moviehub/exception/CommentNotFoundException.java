package com.moviehub.exception;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Exception thrown when a comment is not found in the system.
public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String message) {
        super(message);
    }

}
