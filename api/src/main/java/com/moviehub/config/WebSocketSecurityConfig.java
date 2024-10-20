package com.moviehub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static com.moviehub.entity.Role.ADMIN;
import static com.moviehub.entity.Role.USER;

/// Using a deprecated approach because the non-deprecated one requires a CSRF token to be sent with every WebSocket message.
/// In the non-deprecated version, there is no easy way to disable CSRF.
/// Authorization is handled in the {@link com.moviehub.config.WebSocketAuthorizationInterceptor} using an Auth0 Access Token (JWT), so CSRF is not required.
///
/// @see <a href="https://docs.spring.io/spring-security/reference/servlet/integrations/websocket.html">WebSocket Security</a>
/// @see <a href="https://docs.spring.io/spring-framework/reference/web/websocket/stomp/authentication-token-based.html">Token Authentication</a>
/// @see <a href="https://blog.softbinator.com/overcome-websocket-authentication-issues-stomp/">How to Overcome WebSocket’s Authentication and Authorization Issues</a>
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .simpSubscribeDestMatchers("/topic/chat").hasAnyRole(USER.name(), ADMIN.name())
            .simpDestMatchers("/app/**").hasAnyRole(USER.name(), ADMIN.name())
            .anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}
