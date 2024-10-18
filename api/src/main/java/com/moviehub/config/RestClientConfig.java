package com.moviehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder, Auth0Properties auth0Properties) {
        return builder.baseUrl(auth0Properties.getIssuer())
                      .build();
    }

}
