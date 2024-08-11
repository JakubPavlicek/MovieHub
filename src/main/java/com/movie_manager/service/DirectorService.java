package com.movie_manager.service;

import com.movie_manager.entity.Director;
import com.movie_manager.entity.Director_;
import com.movie_manager.entity.Gender;
import com.movie_manager.exception.DirectorAlreadyExistsException;
import com.movie_manager.exception.DirectorNotFoundException;
import com.movie_manager.repository.DirectorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    private final GenderService genderService;

    @Transactional
    public Director getSavedDirector(Director director) {
        // director is optional
        if (director == null) {
            return null;
        }

        return directorRepository.findByName(director.getName())
                                 .orElseGet(() -> saveDirector(director));
    }

    @Transactional
    public Director addDirector(Director director) {
        verifyDirectorUniqueness(director);

        return saveDirector(director);
    }

    private void verifyDirectorUniqueness(Director director) {
        if (directorRepository.existsByName(director.getName())) {
            throw new DirectorAlreadyExistsException("Director with name: " + director.getName() + " already exists");
        }
    }

    private Director saveDirector(Director director) {
        Gender savedGender = genderService.getSavedGender(director.getGender());
        director.setGender(savedGender);

        return directorRepository.save(director);
    }

    @Transactional
    public Director getDirector(String directorId) {
        return directorRepository.findById(directorId)
                                 .orElseThrow(() -> new DirectorNotFoundException("Director with ID: " + directorId + " not found"));
    }

    @Transactional
    public Page<Director> getDirectors(Integer page, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, Director_.NAME);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return directorRepository.findAll(pageable);
    }

    @Transactional
    public Director updateDirector(String directorId, Director incomingDirector) {
        Director existingDirector = getDirector(directorId);

        if (incomingDirector.getName() != null) {
            verifyDirectorUniqueness(incomingDirector);
            existingDirector.setName(incomingDirector.getName());
        }
        if (incomingDirector.getBio() != null) {
            existingDirector.setBio(incomingDirector.getBio());
        }
        if (incomingDirector.getGender() != null) {
            Gender savedGender = genderService.getSavedGender(incomingDirector.getGender());
            existingDirector.setGender(savedGender);
        }

        return directorRepository.save(existingDirector);
    }

}
