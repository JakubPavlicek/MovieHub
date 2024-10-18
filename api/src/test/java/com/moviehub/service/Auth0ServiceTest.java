package com.moviehub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehub.config.Auth0Properties;
import com.moviehub.config.RestClientConfig;
import com.moviehub.dto.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(Auth0Service.class)
@Import(RestClientConfig.class)
class Auth0ServiceTest {

    @Autowired
    private Auth0Service auth0Service;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Auth0Properties auth0Properties;

    @Test
    void shouldFetchUserInfo() throws JsonProcessingException {
        UserInfo expectedUserInfo = new UserInfo("auth0|123456", "user", "user@example.com", "https://example.com/picture.png");
        String userInfoString = objectMapper.writeValueAsString(expectedUserInfo);

        String accessToken = "accessToken";
        String uri = auth0Properties.getIssuer() + "userinfo?access_token=" + accessToken;
        server.expect(requestTo(uri)).andRespond(withSuccess(userInfoString, MediaType.APPLICATION_JSON));

        UserInfo actualUserInfo = auth0Service.fetchUserInfo(accessToken);

        assertThat(actualUserInfo).isEqualTo(expectedUserInfo);
    }

}