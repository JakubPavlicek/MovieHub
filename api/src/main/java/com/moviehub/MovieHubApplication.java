package com.moviehub;

import com.moviehub.config.Auth0Properties;
import com.moviehub.config.ClientUrlProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Main application class for the Movie Hub application.
@SpringBootApplication
@EnableConfigurationProperties({ClientUrlProperties.class, Auth0Properties.class})
public class MovieHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieHubApplication.class, args);
	}

}
