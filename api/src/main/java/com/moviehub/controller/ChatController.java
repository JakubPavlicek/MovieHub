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

@Log4j2
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final UserService userService;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(String message) {
        log.info("message: {}", message);
        User user = userService.getUser();
        return new ChatMessage(user.getId(), user.getName(), user.getPictureUrl(), message, LocalTime.now().format(TIME_FORMATTER));
    }

}
