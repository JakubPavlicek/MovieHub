package com.moviehub.mapper.dto;

import com.moviehub.dto.GenderDetailsResponse;
import com.moviehub.dto.GenderListResponse;
import com.moviehub.dto.GenderName;
import com.moviehub.entity.Gender;

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
                                    .id(gender.getId())
                                    .name(gender.getName())
                                    .build();
    }

}
