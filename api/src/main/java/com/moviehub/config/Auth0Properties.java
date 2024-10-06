package com.moviehub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "okta.oauth2")
@Getter
@Setter
public class Auth0Properties {

    private String audience;

    private String issuer;

}
