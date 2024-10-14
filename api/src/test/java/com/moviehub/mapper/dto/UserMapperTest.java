package com.moviehub.mapper.dto;

import com.moviehub.dto.UserNameAndPictureUrl;
import com.moviehub.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private static final String NAME = "James";
    private static final String PICTURE_URL = "https://example.com/picture.jpg";
    private static final String EMPTY_NAME = "";
    private static final String NULL_PICTURE_URL = null;

    @Test
    void shouldMapUserToUserNameAndPictureUrl() {
        User user = createUser(NAME, PICTURE_URL);

        UserNameAndPictureUrl userNameAndPictureUrl = UserMapper.toUserNameAndPictureUrl(user);

        assertThat(userNameAndPictureUrl.getName()).isEqualTo(NAME);
        assertThat(userNameAndPictureUrl.getPictureUrl()).isEqualTo(PICTURE_URL);
    }

    @Test
    void shouldHandleNullPictureUrl() {
        User user = createUser(NAME, NULL_PICTURE_URL);

        UserNameAndPictureUrl userNameAndPictureUrl = UserMapper.toUserNameAndPictureUrl(user);

        assertThat(userNameAndPictureUrl.getName()).isEqualTo(NAME);
        assertThat(userNameAndPictureUrl.getPictureUrl()).isNull();
    }

    @Test
    void shouldHandleEmptyName() {
        User user = createUser(EMPTY_NAME, PICTURE_URL);

        UserNameAndPictureUrl userNameAndPictureUrl = UserMapper.toUserNameAndPictureUrl(user);

        assertThat(userNameAndPictureUrl.getName()).isEqualTo(EMPTY_NAME);
        assertThat(userNameAndPictureUrl.getPictureUrl()).isEqualTo(PICTURE_URL);
    }

    private static User createUser(String name, String pictureUrl) {
        return User.builder()
                   .name(name)
                   .pictureUrl(pictureUrl)
                   .build();
    }

}