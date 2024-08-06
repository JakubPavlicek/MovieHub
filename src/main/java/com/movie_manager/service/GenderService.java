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
        // gender can be null -> use default gender name
        String genderName = gender == null ? new Gender().getName() : gender.getName();

        return genderRepository.findByName(genderName)
                               .orElseThrow(() -> new RuntimeException("Gender: " + genderName + " not found"));
    }

}
