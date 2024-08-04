package com.movie_manager.service;

import com.movie_manager.entity.Gender;
import com.movie_manager.repository.GenderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;

    @Transactional
    public Gender getSavedGender(Gender gender) {
        return genderRepository.findByName(gender.getName())
                               .orElseThrow(() -> new RuntimeException("Gender: " + gender.getName() + " not found"));
    }

}
