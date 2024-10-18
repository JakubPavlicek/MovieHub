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

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final Auth0Service auth0Service;

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken)) {
            log.info("user is not authenticated, returning null");
            return null;
        }

        String userId = authentication.getName();

        log.info("retrieving user with ID: {}", userId);

        return userRepository.findById(userId)
                             .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found"));
    }

    public void saveAuthenticatedUser(String userId, String accessToken) {
        if (!userRepository.existsById(userId)) {
            UserInfo userInfo = auth0Service.fetchUserInfo(accessToken);
            saveUserFromInfo(userInfo);
        }
    }

    private void saveUserFromInfo(UserInfo userInfo) {
        User user = User.builder()
                        .id(userInfo.id())
                        .name(userInfo.name())
                        .email(userInfo.email())
                        .pictureUrl(userInfo.pictureUrl())
                        .build();

        userRepository.save(user);
    }

}
