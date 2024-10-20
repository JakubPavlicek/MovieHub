package com.moviehub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Configuration properties for the client application, storing the client URL used
/// for setting allowed origins in WebSocket and other configurations.
@ConfigurationProperties(prefix = "client")
@Getter
@Setter
public class ClientUrlProperties {

    /// The URL of the client application.
    private String url;

}
