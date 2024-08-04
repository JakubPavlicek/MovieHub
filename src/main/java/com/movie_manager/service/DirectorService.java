package com.movie_manager.service;

import com.movie_manager.entity.Director;
import com.movie_manager.repository.DirectorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    @Transactional
    public Director getSavedDirector(Director director) {
        if (director == null) {
            return null;
        }

        return directorRepository.findByName(director.getName())
                                 .orElseGet(() -> directorRepository.save(director));
    }

}
