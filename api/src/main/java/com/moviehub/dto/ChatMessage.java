package com.moviehub.dto;

public record ChatMessage(String userId, String username, String pictureUrl, String message, String time) {
}
