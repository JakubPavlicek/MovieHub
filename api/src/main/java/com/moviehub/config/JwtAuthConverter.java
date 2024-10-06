package com.moviehub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final ClientUrlProperties clientUrlProperties;

    private static final String AUTH0_SUB_PREFIX = "auth0|";

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        String userId = createUserId(jwt);

        return new JwtAuthenticationToken(jwt, authorities, userId);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> userRoles = jwt.getClaimAsStringList(clientUrlProperties.getUrl() + "/roles");

        return userRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
    }

    private String createUserId(Jwt jwt) {
        String sub = jwt.getClaimAsString("sub");
        return sub.substring(AUTH0_SUB_PREFIX.length());
    }

}
