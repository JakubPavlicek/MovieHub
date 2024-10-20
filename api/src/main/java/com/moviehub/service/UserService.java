package com.moviehub.service;

import com.moviehub.dto.UserInfo;
import com.moviehub.entity.User;
import com.moviehub.exception.UserNotFoundException;
import com.moviehub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing user-related operations in the MovieHub application.
/// This class handles retrieving and saving authenticated users.
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService {

    /// Repository for handling user entities in the database.
    private final UserRepository userRepository;

    /// Service for managing Auth0-related operations.
    private final Auth0Service auth0Service;

    /// Retrieves the currently authenticated user from the security context.
    ///
    /// @return The User entity of the authenticated user.
    /// @throws UserNotFoundException if the user is not found in the database.
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken)) {
            log.debug("user is not authenticated, returning null");
            return null;
        }

        String userId = authentication.getName();

        log.info("retrieving user from database");

        return userRepository.findById(userId)
                             .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found"));
    }

    /// Saves the authenticated user in the database if they do not already exist.
    ///
    /// @param userId The ID of the authenticated user.
    /// @param accessToken The access token used to fetch user information from Auth0.
    public void saveAuthenticatedUser(String userId, String accessToken) {
        if (!userRepository.existsById(userId)) {
            log.info("saving user");
            UserInfo userInfo = auth0Service.fetchUserInfo(accessToken);
            saveUserFromInfo(userInfo);
        }
    }

    /// Saves a User entity from the provided UserInfo object.
    ///
    /// @param userInfo The UserInfo object containing user details.
    private void saveUserFromInfo(UserInfo userInfo) {
        User user = User.builder()
                        .id(userInfo.id())
                        .name(userInfo.name())
                        .email(userInfo.email())
                        .pictureUrl(userInfo.pictureUrl())
                        .build();

        userRepository.save(user);

        log.info("user saved");
    }

}
