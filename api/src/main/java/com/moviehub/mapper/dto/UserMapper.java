package com.moviehub.mapper.dto;

import com.moviehub.dto.UserNameAndPictureUrl;
import com.moviehub.entity.User;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting User entities to UserNameAndPictureUrl DTOs.
public class UserMapper {

    // Private constructor to prevent instantiation.
    private UserMapper() {
    }

    /// Converts a User entity to a UserNameAndPictureUrl DTO.
    ///
    /// @param user The User entity to convert.
    /// @return The corresponding UserNameAndPictureUrl DTO.
    public static UserNameAndPictureUrl toUserNameAndPictureUrl(User user) {
        return UserNameAndPictureUrl.builder()
                                    .name(user.getName())
                                    .pictureUrl(user.getPictureUrl())
                                    .build();
    }

}
