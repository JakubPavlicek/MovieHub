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
        return directorRepository.findByName(director.getName())
                                 .orElseGet(() -> saveDirector(director));
    }

    public Director addDirector(Director director) {
        verifyDirectorUniqueness(director.getName(), director.getId());

        return saveDirector(director);
    }

    private void verifyDirectorUniqueness(String directorName, UUID directorId) {
        boolean isNameTaken;

        if (directorId != null) {
            isNameTaken = directorRepository.existsByNameAndIdNot(directorName, directorId);
        } else {
            isNameTaken = directorRepository.existsByName(directorName);
        }

        if (isNameTaken) {
            throw new DirectorAlreadyExistsException("Director with name: " + directorName + " already exists");
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
        Sort sort = Sort.by(Director_.NAME).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return directorRepository.findAll(pageable);
        }

        return directorRepository.findAllByName(name, pageable);
    }

    public Director updateDirector(UUID directorId, Director incomingDirector) {
        Director existingDirector = getDirector(directorId);

        verifyDirectorUniqueness(incomingDirector.getName(), directorId);

        existingDirector.setName(incomingDirector.getName());
        existingDirector.setBio(incomingDirector.getBio());
        existingDirector.setGender(incomingDirector.getGender());

        return directorRepository.save(existingDirector);
    }

}
