package com.moviehub.controller;

import com.moviehub.dto.ChatMessage;
import com.moviehub.entity.User;
import com.moviehub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Controller class for handling chat messages.
/// It listens for incoming chat messages and broadcasts them to all connected clients.
@Controller
@Log4j2
@RequiredArgsConstructor
public class ChatController {

    /// Service for retrieving user information.
    private final UserService userService;

    /// Formatter for formatting the current time in HH:mm format.
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /// Handles incoming chat messages and broadcasts them to all subscribed clients.
    ///
    /// @param message The chat message sent by the client.
    /// @return A {@link ChatMessage} object containing the user information, the message, and the current time.
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(String message) {
        log.info("message: {}", message);
        User user = userService.getUser();
        return new ChatMessage(user.getId(), user.getName(), user.getPictureUrl(), message, LocalTime.now().format(TIME_FORMATTER));
    }

}
