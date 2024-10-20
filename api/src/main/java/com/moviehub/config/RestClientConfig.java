package com.moviehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Configuration class for setting up a RestClient bean used to communicate with
/// external services, configured with properties from Auth0.
@Configuration
public class RestClientConfig {

    /// Creates a RestClient bean, configuring it with the base URL obtained from
    /// Auth0 properties.
    ///
    /// @param builder the RestClient.Builder used to create the RestClient instance
    /// @param auth0Properties properties containing the Auth0 issuer URL
    /// @return a configured RestClient instance
    @Bean
    public RestClient restClient(RestClient.Builder builder, Auth0Properties auth0Properties) {
        return builder.baseUrl(auth0Properties.getIssuer())
                      .build();
    }

}

