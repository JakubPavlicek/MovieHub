package com.moviehub.service;

import com.moviehub.config.Auth0Properties;
import com.moviehub.dto.UserInfo;
import com.moviehub.entity.User;
import com.moviehub.exception.UserNotFoundException;
import com.moviehub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken)) {
            log.info("user is not authenticated, returning null");
            return null;
        }

        String userId = authentication.getName();

        log.info("getting user with ID: {}", userId);

        return userRepository.findById(userId)
                             .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found"));
    }

    public void saveAuthenticatedUser(String userId, String accessToken) {
        if (!userRepository.existsById(userId)) {
            UserInfo userInfo = fetchUserInfo(accessToken);
            createUser(userInfo);
        }
    }

    private void createUser(UserInfo userInfo) {
        User user = User.builder()
                        .id(userInfo.id())
                        .name(userInfo.name())
                        .email(userInfo.email())
                        .pictureUrl(userInfo.pictureUrl())
                        .build();

        userRepository.save(user);
    }

    private UserInfo fetchUserInfo(String accessToken) {
        UserInfo userInfo = restClient.get()
                                      .uri("/userinfo?access_token=" + accessToken)
                                      .retrieve()
                                      .body(UserInfo.class);

        log.info("fetching userInfo");

        return userInfo;
    }

}
