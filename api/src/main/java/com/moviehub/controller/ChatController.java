package com.moviehub.controller;

import com.moviehub.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(@AuthenticationPrincipal Principal principal, String message) {
        return new ChatMessage(principal.getName().split("\\|")[0], message, LocalTime.now().format(TIME_FORMATTER));
    }

}
