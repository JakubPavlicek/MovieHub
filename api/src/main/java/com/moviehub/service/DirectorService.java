package com.moviehub.service;

import com.moviehub.entity.Director;
import com.moviehub.entity.Director_;
import com.moviehub.exception.DirectorAlreadyExistsException;
import com.moviehub.exception.DirectorNotFoundException;
import com.moviehub.repository.DirectorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    public Director getSavedDirector(Director director) {
        // director is optional
        if (director == null) {
            return null;
        }

        return directorRepository.findByName(director.getName())
                                 .orElseGet(() -> saveDirector(director));
    }

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
        return directorRepository.save(director);
    }

    public Director getDirector(UUID directorId) {
        return directorRepository.findById(directorId)
                                 .orElseThrow(() -> new DirectorNotFoundException("Director with ID: " + directorId + " not found"));
    }

    public Page<Director> getDirectors(Integer page, Integer limit, String name) {
        Sort sort = Sort.by(Sort.Direction.ASC, Director_.NAME);
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return directorRepository.findAll(pageable);
        }

        return directorRepository.findAllByName(name, pageable);
    }

    public Director updateDirector(UUID directorId, Director incomingDirector) {
        Director existingDirector = getDirector(directorId);

        if (incomingDirector.getName() != null) {
            verifyDirectorUniqueness(incomingDirector);
            existingDirector.setName(incomingDirector.getName());
        }
        if (incomingDirector.getBio() != null) {
            existingDirector.setBio(incomingDirector.getBio());
        }
        if (incomingDirector.getGender() != null) {
            existingDirector.setGender(incomingDirector.getGender());
        }

        return directorRepository.save(existingDirector);
    }

}
