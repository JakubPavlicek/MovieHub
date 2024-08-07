package com.movie_manager.mapper.dto;

import com.movie_manager.dto.GenderDTO;
import com.movie_manager.dto.GenderName;
import com.movie_manager.dto.GenderResponse;
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

    public static GenderResponse mapToGenderResponse(List<Gender> genders) {
        List<GenderDTO> genderDTOS = genders.stream()
                                            .map(GenderMapper::mapToGenderDTO)
                                            .toList();

        GenderResponse genderResponse = new GenderResponse();
        genderResponse.setGenders(genderDTOS);

        return genderResponse;
    }

    private static GenderDTO mapToGenderDTO(Gender gender) {
        return GenderDTO.builder()
                        .genderId(gender.getGenderId())
                        .name(gender.getName())
                        .build();
    }

}
