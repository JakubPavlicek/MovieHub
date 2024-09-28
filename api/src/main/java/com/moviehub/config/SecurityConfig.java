package com.moviehub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    private final ClientUrlProperties clientUrlProperties;

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize ->
                authorize.requestMatchers(GET).permitAll()
                         .requestMatchers(POST, "/movies", "/directors", "/actors", "/production-companies", "/genres", "/countries").hasRole(ADMIN)
                         .requestMatchers(POST, "/movies/*/comments", "/comments/*/reaction").hasAnyRole(USER, ADMIN)
                         .requestMatchers(PUT, "/movies/*", "/directors/*", "/actors/*", "/production-companies/*", "/genres/*", "/countries/*").hasRole(ADMIN)
                         .requestMatchers(PUT, "/movies/*/rating").hasAnyRole(USER, ADMIN)
                         .requestMatchers(DELETE, "/movies/*").hasRole(ADMIN)
                         .requestMatchers(DELETE, "/comments/*").hasAnyRole(USER, ADMIN)
                         .anyRequest().hasRole(ADMIN)
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(clientUrlProperties.getUrl());
            }
        };
    }

}
