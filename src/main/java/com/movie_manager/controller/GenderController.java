package com.movie_manager.controller;

import com.movie_manager.GendersApi;
import com.movie_manager.dto.GenderResponse;
import com.movie_manager.entity.Gender;
import com.movie_manager.mapper.dto.GenderMapper;
import com.movie_manager.service.GenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class GenderController implements GendersApi {

    private final GenderService genderService;

    @Override
    public ResponseEntity<GenderResponse> getGendres() {
        List<Gender> genders = genderService.getGendres();
        GenderResponse genderResponse = GenderMapper.mapToGenderResponse(genders);

        return ResponseEntity.ok(genderResponse);
    }

}
