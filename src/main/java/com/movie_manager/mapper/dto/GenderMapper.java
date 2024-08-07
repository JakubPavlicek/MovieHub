package com.movie_manager.mapper.dto;

import com.movie_manager.dto.GenderName;
import com.movie_manager.entity.Gender;

public class GenderMapper {

    private GenderMapper() {
    }

    public static Gender mapToGender(GenderName genderName) {
        return Gender.builder()
                     .name(genderName == null ? GenderName.UNSPECIFIED.getValue() : genderName.getValue())
                     .build();
    }

}
