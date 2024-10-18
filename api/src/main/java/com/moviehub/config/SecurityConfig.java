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

import static com.moviehub.entity.Role.ADMIN;
import static com.moviehub.entity.Role.USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    private final ClientUrlProperties clientUrlProperties;
    private final Auth0Properties auth0Properties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize ->
                authorize.requestMatchers(OPTIONS).permitAll()
                         .requestMatchers(GET, "/movies/*/ratings/me").hasAnyRole(USER.name(), ADMIN.name())
                         .requestMatchers(GET).permitAll()
                         .requestMatchers(POST, "/movies", "/directors", "/actors", "/production-companies", "/genres", "/countries").hasRole(ADMIN.name())
                         .requestMatchers(POST, "/movies/*/comments", "/comments/*/reactions", "/movies/*/ratings", "/comments/*/replies", "/replies/*/reactions").hasAnyRole(USER.name(), ADMIN.name())
                         .requestMatchers(PUT, "/movies/*", "/directors/*", "/actors/*", "/production-companies/*", "/genres/*", "/countries/*").hasRole(ADMIN.name())
                         .requestMatchers(DELETE, "/movies/*").hasRole(ADMIN.name())
                         .requestMatchers(DELETE, "/comments/*", "/replies/*").hasAnyRole(USER.name(), ADMIN.name())
                         .anyRequest().hasRole(ADMIN.name())
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
                registry.addMapping("/**")
                        .allowedOrigins(clientUrlProperties.getUrl())
                        .allowedMethods(GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name());
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(auth0Properties.getIssuer());
    }

}
