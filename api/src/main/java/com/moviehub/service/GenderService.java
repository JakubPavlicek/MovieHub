package com.moviehub.service;

import com.moviehub.dto.GenderName;
import com.moviehub.entity.Gender;
import com.moviehub.repository.GenderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;

    @Transactional
    public Gender getSavedGender(Gender gender) {
        // gender can be null -> use default gender name
        String genderName = gender == null ? GenderName.UNSPECIFIED.getValue() : gender.getName();

        return genderRepository.findByName(genderName)
                               .orElseThrow(() -> new RuntimeException("Gender: " + genderName + " not found"));
    }

    @Transactional
    public List<Gender> getGendres() {
        return genderRepository.findAll();
    }

}
