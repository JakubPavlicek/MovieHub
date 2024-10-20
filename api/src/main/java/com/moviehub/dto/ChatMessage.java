package com.moviehub.dto;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Record representing a chat message.
public record ChatMessage(
    String userId,
    String username,
    String pictureUrl,
    String message,
    String time
) {

}
