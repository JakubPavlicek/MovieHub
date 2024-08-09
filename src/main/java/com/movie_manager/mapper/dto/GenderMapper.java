package com.movie_manager.mapper.dto;

import com.movie_manager.dto.GenderDetailsResponse;
import com.movie_manager.dto.GenderListResponse;
import com.movie_manager.dto.GenderName;
import com.movie_manager.entity.Gender;

import java.util.List;

public class GenderMapper {

    private GenderMapper() {
    }

    public static Gender mapToGender(GenderName genderName) {
        return Gender.builder()
                     .name(genderName == null ? GenderName.UNSPECIFIED.getValue() : genderName.getValue())
                     .build();
    }

    public static GenderListResponse mapToGenderResponse(List<Gender> genders) {
        List<GenderDetailsResponse> genderDetailsResponses = genders.stream()
                                                                    .map(GenderMapper::mapToGenderDetailsResponse)
                                                                    .toList();

        GenderListResponse genderResponse = new GenderListResponse();
        genderResponse.setGenders(genderDetailsResponses);

        return genderResponse;
    }

    private static GenderDetailsResponse mapToGenderDetailsResponse(Gender gender) {
        return GenderDetailsResponse.builder()
                                    .genderId(gender.getGenderId())
                                    .name(gender.getName())
                                    .build();
    }

}
