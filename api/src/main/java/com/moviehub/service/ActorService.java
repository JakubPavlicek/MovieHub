package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Actor_;
import com.moviehub.exception.ActorAlreadyExistsException;
import com.moviehub.exception.ActorNotFoundException;
import com.moviehub.repository.ActorRepository;
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
/// Service class for managing Actor entities.
/// This class provides methods for adding, retrieving, updating, and verifying actors.
@Service
@Transactional
@Log4j2
@AllArgsConstructor
public class ActorService {

    /// Repository for managing Actor entities.
    private final ActorRepository actorRepository;

    /// Retrieves a saved Actor by their name or saves a new Actor if not found.
    ///
    /// @param actor The Actor entity to retrieve or save.
    /// @return The retrieved or newly saved Actor entity.
    public Actor getSavedActor(Actor actor) {
        log.info("retrieving saved actor: {}", actor.getName());

        return actorRepository.findByName(actor.getName())
                              .orElseGet(() -> saveActor(actor));
    }

    /// Adds a new Actor to the repository.
    ///
    /// @param actor The Actor entity to add.
    /// @return The newly added Actor entity.
    public Actor addActor(Actor actor) {
        log.info("adding actor: {}", actor.getName());

        verifyActorUniqueness(actor.getName());

        return saveActor(actor);
    }

    /// Verifies the uniqueness of the Actor's name.
    /// Throws an exception if an Actor with the same name already exists.
    ///
    /// @param actorName The name of the Actor to verify.
    private void verifyActorUniqueness(String actorName) {
        log.debug("verifying actor uniqueness: {}", actorName);

        if (actorName != null && actorRepository.existsByName(actorName)) {
            throw new ActorAlreadyExistsException("Actor with name: " + actorName + " already exists");
        }
    }

    /// Saves a new Actor to the repository.
    ///
    /// @param actor The Actor entity to save.
    /// @return The saved Actor entity.
    private Actor saveActor(Actor actor) {
        log.info("saving actor: {}", actor.getName());
        return actorRepository.save(actor);
    }

    /// Retrieves an Actor by their ID.
    /// Throws an exception if the Actor is not found.
    ///
    /// @param actorId The ID of the Actor to retrieve.
    /// @return The retrieved Actor entity.
    public Actor getActor(UUID actorId) {
        log.info("retrieving actor: {}", actorId);

        return actorRepository.findById(actorId)
                              .orElseThrow(() -> new ActorNotFoundException("Actor with ID: " + actorId + " not found"));
    }

    /// Retrieves a paginated list of Actors, optionally filtered by name.
    ///
    /// @param page The page number to retrieve.
    /// @param limit The number of Actors per page.
    /// @param name The name to filter Actors by (optional).
    /// @return A Page containing the retrieved Actor entities.
    public Page<Actor> getActors(Integer page, Integer limit, String name) {
        log.info("retrieving actors with page: {}, limit: {}, name: {}", page, limit, name);

        Sort sort = Sort.by(Actor_.NAME).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return actorRepository.findAll(pageable);
        }

        return actorRepository.findAllByName(name, pageable);
    }

    /// Updates an existing Actor's details.
    ///
    /// @param actorId The ID of the Actor to update.
    /// @param incomingActor The Actor entity containing updated details.
    /// @return The updated Actor entity.
    public Actor updateActor(UUID actorId, Actor incomingActor) {
        log.info("updating actor: {}", actorId);

        Actor existingActor = getActor(actorId);

        verifyActorUniqueness(incomingActor.getName());

        existingActor.setName(incomingActor.getName());
        existingActor.setBio(incomingActor.getBio());
        existingActor.setGender(incomingActor.getGender());

        return actorRepository.save(existingActor);
    }

}
