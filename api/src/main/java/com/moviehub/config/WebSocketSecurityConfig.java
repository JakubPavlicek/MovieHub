package com.moviehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Bean
    AuthorizationManager<Message<?>> authorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
            .simpSubscribeDestMatchers("/topic/chat").hasAnyRole(USER, ADMIN)
            .simpDestMatchers("/app/**").hasAnyRole(USER, ADMIN)
            .anyMessage().denyAll()
            .build();
    }

    @Bean
    public ChannelInterceptor csrfChannelInterceptor(){
        // disable csrf
        return new ChannelInterceptor() {
        };
    }

}
