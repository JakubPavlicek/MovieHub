package com.moviehub.service;

import com.moviehub.config.Auth0Properties;
import com.moviehub.dto.UserInfo;
import com.moviehub.entity.User;
import com.moviehub.exception.UserNotFoundException;
import com.moviehub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Transactional
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    private final RestClient restClient;

    public UserService(UserRepository userRepository, Auth0Properties auth0Properties) {
        this.userRepository = userRepository;

        restClient = RestClient.builder()
                               .baseUrl(auth0Properties.getIssuer())
                               .build();
    }

    public User getUser() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken)) {
            log.info("user is not authenticated, returning null");
            return null;
        }

        String userId = SecurityContextHolder.getContext()
                                             .getAuthentication()
                                             .getName();

        log.info("userId: {}", userId);

        if (!userRepository.existsById(userId)) {
            UserInfo userInfo = fetchUserInfo();
            return createUser(userInfo);
        }

        return userRepository.findById(userId)
                             .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found"));
    }

    private User createUser(UserInfo userInfo) {
        User user = User.builder()
                        .id(userInfo.id())
                        .name(userInfo.name())
                        .email(userInfo.email())
                        .pictureUrl(userInfo.pictureUrl())
                        .build();

        return userRepository.save(user);
    }

    private UserInfo fetchUserInfo() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String accessToken = authentication.getToken().getTokenValue();

        UserInfo userInfo = restClient.get()
                                      .uri("/userinfo?access_token=" + accessToken)
                                      .retrieve()
                                      .body(UserInfo.class);

        log.info("fetching userInfo: {}", userInfo);

        return userInfo;
    }

}
