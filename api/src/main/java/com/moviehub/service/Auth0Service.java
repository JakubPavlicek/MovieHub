package com.moviehub.service;

import com.moviehub.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Log4j2
@Service
@RequiredArgsConstructor
public class Auth0Service {

    private final RestClient restClient;

    public UserInfo fetchUserInfo(String accessToken) {
        UserInfo userInfo = restClient.get()
                                      .uri("/userinfo?access_token={accessToken}", accessToken)
                                      .retrieve()
                                      .body(UserInfo.class);

        log.info("fetched user's info");

        return userInfo;
    }

}
