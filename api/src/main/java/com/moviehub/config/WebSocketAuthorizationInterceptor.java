package com.moviehub.config;

import com.moviehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
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

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Interceptor for authorizing WebSocket connections by validating JWT tokens
/// provided in the STOMP CONNECT frame and setting the security context for authenticated users.
@Component
@RequiredArgsConstructor
public class WebSocketAuthorizationInterceptor implements ChannelInterceptor {

    /// Decoder for processing JWT tokens to validate their authenticity.
    private final JwtDecoder jwtDecoder;
    /// Converter for transforming JWT tokens into authentication tokens with authorities.
    private final JwtAuthConverter jwtAuthConverter;
    /// Service for managing user-related operations, including saving authenticated user information.
    private final UserService userService;

    /// Intercepts messages before they are sent, allowing for authorization of WebSocket connections.
    ///
    /// @param message the message being sent to the channel
    /// @param channel the channel through which the message is being sent
    /// @return the message
    /// @throws AccessDeniedException if authorization fails
    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // check if the command is STOMP CONNECT
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader(AUTHORIZATION);
            String bearerPrefix = BEARER.getValue() + " ";

            // validate the presence and format of the Authorization header
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
