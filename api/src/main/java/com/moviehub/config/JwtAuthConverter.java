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

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Converter that transforms a Jwt token into a JwtAuthenticationToken,
/// extracting user roles and authorities from the JWT claims.
@Component
@RequiredArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    /// Properties for client configuration, used to retrieve the roles claim.
    private final ClientUrlProperties clientUrlProperties;

    /// Converts a Jwt token into a JwtAuthenticationToken, extracting authorities
    /// from the JWT claims.
    ///
    /// @param jwt the JWT token to be converted
    /// @return a JwtAuthenticationToken containing the authorities and subject
    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("sub"));
    }

    /// Extracts user roles from the JWT claims and converts them into GrantedAuthority objects.
    ///
    /// @param jwt the JWT token from which to extract roles
    /// @return a collection of GrantedAuthority based on the roles found in the JWT
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> userRoles = jwt.getClaimAsStringList(clientUrlProperties.getUrl() + "/roles");

        return userRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
    }

}
