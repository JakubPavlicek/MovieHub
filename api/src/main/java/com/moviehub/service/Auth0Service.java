package com.moviehub.service;

import com.moviehub.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for interacting with Auth0 for user authentication.
/// This class provides methods for fetching user information using an access token.
@Service
@Log4j2
@RequiredArgsConstructor
public class Auth0Service {

    /// Client for making REST API calls to Auth0.
    private final RestClient restClient;

    /// Fetches user information from Auth0 using the provided access token.
    ///
    /// @param accessToken The access token for authenticating the request.
    /// @return UserInfo object containing the user's details.
    public UserInfo fetchUserInfo(String accessToken) {
        UserInfo userInfo = restClient.get()
                                      .uri("/userinfo?access_token={accessToken}", accessToken)
                                      .retrieve()
                                      .body(UserInfo.class);

        log.info("fetched user's info");

        return userInfo;
    }

}
