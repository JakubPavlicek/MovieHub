package com.moviehub.controller;

import com.moviehub.GendersApi;
import com.moviehub.dto.GenderListResponse;
import com.moviehub.entity.Gender;
import com.moviehub.mapper.dto.GenderMapper;
import com.moviehub.service.GenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class GenderController implements GendersApi {

    private final GenderService genderService;

    @Override
    public ResponseEntity<GenderListResponse> getGendres() {
        List<Gender> genders = genderService.getGendres();
        GenderListResponse genderListResponse = GenderMapper.mapToGenderResponse(genders);

        return ResponseEntity.ok(genderListResponse);
    }

}
