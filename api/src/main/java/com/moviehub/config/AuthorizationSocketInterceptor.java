package com.moviehub.config;

import com.moviehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Component
@RequiredArgsConstructor
public class AuthorizationSocketInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthConverter jwtAuthConverter;
    private final UserService userService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader(AUTHORIZATION);
            String bearerPrefix = BEARER.getValue() + " ";

            if (authHeader == null || !authHeader.startsWith(bearerPrefix)) {
                throw new AccessDeniedException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(bearerPrefix.length());

            Jwt jwt = jwtDecoder.decode(token);

            if (jwt == null) {
                return message;
            }

            // set security context
            JwtAuthenticationToken jwtAuthentication = jwtAuthConverter.convert(jwt);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            accessor.setUser(jwtAuthentication);

            if (jwtAuthentication == null) {
                return message;
            }

            // save authenticated user
            String userId = jwtAuthentication.getName();
            String accessToken = jwtAuthentication.getToken().getTokenValue();

            userService.saveAuthenticatedUser(userId, accessToken);
        }

        return message;
    }

}
