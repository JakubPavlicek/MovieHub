package com.moviehub.mapper.dto;

import com.moviehub.dto.UserNameAndPictureUrl;
import com.moviehub.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserNameAndPictureUrl toUserNameAndPictureUrl(User user) {
        return UserNameAndPictureUrl.builder()
                                    .name(user.getName())
                                    .pictureUrl(user.getPictureUrl())
                                    .build();
    }

}
