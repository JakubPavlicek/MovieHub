package com.moviehub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Configuration properties for Auth0 OAuth2 integration, providing the audience and issuer
/// details required for token validation.
@ConfigurationProperties(prefix = "okta.oauth2")
@Getter
@Setter
public class Auth0Properties {

    /// The audience for Auth0 OAuth2 tokens.
    private String audience;

    /// The issuer for Auth0 OAuth2 tokens.
    private String issuer;

}
