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

@Service
@Transactional
@Log4j2
@AllArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    public Actor getSavedActor(Actor actor) {
        log.info("retrieving saved actor: {}", actor.getName());

        return actorRepository.findByName(actor.getName())
                              .orElseGet(() -> saveActor(actor));
    }

    public Actor addActor(Actor actor) {
        log.info("adding actor: {}", actor.getName());

        verifyActorUniqueness(actor.getName());

        return saveActor(actor);
    }

    private void verifyActorUniqueness(String actorName) {
        log.debug("verifying actor uniqueness: {}", actorName);

        if (actorName != null && actorRepository.existsByName(actorName)) {
            throw new ActorAlreadyExistsException("Actor with name: " + actorName + " already exists");
        }
    }

    private Actor saveActor(Actor actor) {
        log.info("saving actor: {}", actor.getName());
        return actorRepository.save(actor);
    }

    public Actor getActor(UUID actorId) {
        log.info("retrieving actor: {}", actorId);

        return actorRepository.findById(actorId)
                              .orElseThrow(() -> new ActorNotFoundException("Actor with ID: " + actorId + " not found"));
    }

    public Page<Actor> getActors(Integer page, Integer limit, String name) {
        log.info("retrieving actors with page: {}, limit: {}, name: {}", page, limit, name);

        Sort sort = Sort.by(Actor_.NAME).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return actorRepository.findAll(pageable);
        }

        return actorRepository.findAllByName(name, pageable);
    }

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
