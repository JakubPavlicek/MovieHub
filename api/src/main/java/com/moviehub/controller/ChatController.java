package com.moviehub.controller;

import com.moviehub.dto.ChatMessage;
import com.moviehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final UserService userService;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(String message) {
        return new ChatMessage(userService.getUser().getName(), message, LocalTime.now().format(TIME_FORMATTER));
    }

}
