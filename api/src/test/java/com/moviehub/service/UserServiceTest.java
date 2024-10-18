package com.moviehub.service;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.config.ClientUrlProperties;
import com.moviehub.config.JwtAuthConverter;
import com.moviehub.config.SecurityConfig;
import com.moviehub.dto.UserInfo;
import com.moviehub.entity.User;
import com.moviehub.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import({SecurityConfig.class, TestcontainersConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    @Autowired
    private ClientUrlProperties clientUrlProperties;

    @MockBean
    private Auth0Service auth0Service;

    @Test
    void shouldGetUser() {
        Jwt jwt = Jwt.withTokenValue("mocked-token")
                     .header("alg", "RS256")
                     .claim("sub", "auth0|66f8511afb3983f9045fc7c9")
                     .claim(clientUrlProperties.getUrl() + "/roles", "USER")
                     .build();

        JwtAuthenticationToken jwtAuthenticationToken = jwtAuthConverter.convert(jwt);

        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);

        User user = userService.getUser();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo("auth0|66f8511afb3983f9045fc7c9");
        assertThat(user.getName()).isEqualTo("user");
        assertThat(user.getEmail()).isEqualTo("user@user.cz");
        assertThat(user.getPictureUrl()).isEqualTo("https://s.gravatar.com/avatar/4d2acb461e1103a88a20aa53481819ba?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fus.png");
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNameDoesNotExist() {
        Jwt jwt = Jwt.withTokenValue("mocked-token")
                     .header("alg", "RS256")
                     .claim("sub", "")
                     .claim(clientUrlProperties.getUrl() + "/roles", "USER")
                     .build();

        JwtAuthenticationToken jwtAuthenticationToken = jwtAuthConverter.convert(jwt);

        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);

        assertThatThrownBy(() -> userService.getUser()).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldNotGetUserWhenUserIsNotAuthenticated() {
        User user = userService.getUser();

        assertThat(user).isNull();
    }

    @Test
    void shouldSaveNewAuthenticatedUser() {
        UserInfo userInfo = new UserInfo("auth0|66f8511afb3983f9045fc7c8", "user", "user@gmail.com", "https://s.gravatar.com/avatar/4d2acb46.png");

        when(auth0Service.fetchUserInfo(anyString())).thenReturn(userInfo);

        userService.saveAuthenticatedUser("auth0|66f8511afb3983f9045fc7c8", "accessToken");

        verify(auth0Service).fetchUserInfo("accessToken");
    }

    @Test
    void shouldNotSaveAuthenticatedUser() {
        userService.saveAuthenticatedUser("auth0|66f8511afb3983f9045fc7c9", "accessToken");

        verify(auth0Service, times(0)).fetchUserInfo("accessToken");
    }

}