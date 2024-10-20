package com.moviehub.service;

import com.moviehub.entity.Director;
import com.moviehub.entity.Director_;
import com.moviehub.exception.DirectorAlreadyExistsException;
import com.moviehub.exception.DirectorNotFoundException;
import com.moviehub.repository.DirectorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing directors.
/// This class provides methods for adding, retrieving, and updating director entities.
@Service
@Transactional
@Log4j2
@AllArgsConstructor
public class DirectorService {

    /// Repository for managing Director entities.
    private final DirectorRepository directorRepository;

    /// Retrieves a saved director by name or saves a new one if not found.
    ///
    /// @param director The Director entity to check for existence.
    /// @return The existing Director entity if found, otherwise saves and returns the new one.
    public Director getSavedDirector(Director director) {
        log.info("retrieving saved director: {}", director.getName());

        return directorRepository.findByName(director.getName())
                                 .orElseGet(() -> saveDirector(director));
    }

    /// Adds a new director to the database.
    /// Throws a DirectorAlreadyExistsException if a director with the same name already exists.
    ///
    /// @param director The Director entity to add.
    /// @return The newly created Director entity.
    public Director addDirector(Director director) {
        log.info("adding director: {}", director.getName());

        verifyDirectorUniqueness(director.getName(), director.getId());

        return saveDirector(director);
    }

    /// Verifies the uniqueness of the director's name.
    /// Throws a DirectorAlreadyExistsException if a director with the same name exists.
    ///
    /// @param directorName The name of the director to check.
    /// @param directorId The ID of the director (if updating).
    private void verifyDirectorUniqueness(String directorName, UUID directorId) {
        log.debug("verifying director uniqueness: {}", directorName);

        boolean isNameTaken;

        if (directorId != null) {
            isNameTaken = directorRepository.existsByNameAndIdNot(directorName, directorId);
        }
        else {
            isNameTaken = directorRepository.existsByName(directorName);
        }

        if (isNameTaken) {
            throw new DirectorAlreadyExistsException("Director with name: " + directorName + " already exists");
        }
    }

    /// Saves a director to the database.
    ///
    /// @param director The Director entity to save.
    /// @return The saved Director entity.
    private Director saveDirector(Director director) {
        log.info("saving director: {}", director.getName());
        return directorRepository.save(director);
    }

    /// Retrieves a director by their ID.
    /// Throws a DirectorNotFoundException if the director does not exist.
    ///
    /// @param directorId The ID of the director to retrieve.
    /// @return The Director entity associated with the specified ID.
    public Director getDirector(UUID directorId) {
        log.info("retrieving director: {}", directorId);

        return directorRepository.findById(directorId)
                                 .orElseThrow(() -> new DirectorNotFoundException("Director with ID: " + directorId + " not found"));
    }

    /// Retrieves a paginated list of directors, optionally filtered by name.
    ///
    /// @param page The page number to retrieve.
    /// @param limit The number of directors per page.
    /// @param name The optional name filter for the directors.
    /// @return A page of Director entities.
    public Page<Director> getDirectors(Integer page, Integer limit, String name) {
        log.info("retrieving directors with page: {}, limit: {}, name: {}", page, limit, name);

        Sort sort = Sort.by(Director_.NAME).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return directorRepository.findAll(pageable);
        }

        return directorRepository.findAllByName(name, pageable);
    }

    /// Updates an existing director's details.
    /// Throws a DirectorNotFoundException if the director does not exist.
    /// Throws a DirectorAlreadyExistsException if the updated name already exists.
    ///
    /// @param directorId The ID of the director to update.
    /// @param incomingDirector The Director entity containing the new details.
    /// @return The updated Director entity.
    public Director updateDirector(UUID directorId, Director incomingDirector) {
        log.info("updating director: {}", directorId);

        Director existingDirector = getDirector(directorId);

        verifyDirectorUniqueness(incomingDirector.getName(), directorId);

        existingDirector.setName(incomingDirector.getName());
        existingDirector.setBio(incomingDirector.getBio());
        existingDirector.setGender(incomingDirector.getGender());

        return directorRepository.save(existingDirector);
    }

}
