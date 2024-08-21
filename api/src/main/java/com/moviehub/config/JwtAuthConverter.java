package com.moviehub.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private static final String CLIENT_ID = "api";

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("preferred_username"));
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> clientRoles = jwt.getClaimAsMap("resource_access").get(CLIENT_ID) != null
            ? (List<String>) ((Map<String, Object>) jwt.getClaimAsMap("resource_access").get(CLIENT_ID)).get("roles")
            : List.of();

        return clientRoles.stream()
                          .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                          .collect(Collectors.toList());
    }

}
