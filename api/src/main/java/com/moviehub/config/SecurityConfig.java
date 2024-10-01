package com.moviehub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
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
@Order(1)
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
                         .requestMatchers(GET, "/movies/*/ratings").hasAnyRole(USER, ADMIN)
                         .requestMatchers(POST, "/movies", "/directors", "/actors", "/production-companies", "/genres", "/countries").hasRole(ADMIN)
                         .requestMatchers(POST, "/movies/*/comments", "/comments/*/reactions").hasAnyRole(USER, ADMIN)
                         .requestMatchers(POST, "/movies/*/ratings").hasAnyRole(USER, ADMIN)
                         .requestMatchers(PUT, "/movies/*", "/directors/*", "/actors/*", "/production-companies/*", "/genres/*", "/countries/*").hasRole(ADMIN)
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

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://dev-bzrzau6cj3i0m33i.us.auth0.com/");
    }

}
