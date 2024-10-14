package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Actor_;
import com.moviehub.exception.ActorAlreadyExistsException;
import com.moviehub.exception.ActorNotFoundException;
import com.moviehub.repository.ActorRepository;
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
public class ActorService {

    private final ActorRepository actorRepository;

    public Actor getSavedActor(Actor actor) {
        return actorRepository.findByName(actor.getName())
                              .orElseGet(() -> saveActor(actor));
    }

    public Actor addActor(Actor actor) {
        verifyActorUniqueness(actor.getName());

        return saveActor(actor);
    }

    private void verifyActorUniqueness(String actorName) {
        if (actorName != null && actorRepository.existsByName(actorName)) {
            throw new ActorAlreadyExistsException("Actor with name: " + actorName + " already exists");
        }
    }

    private Actor saveActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public Actor getActor(UUID actorId) {
        return actorRepository.findById(actorId)
                              .orElseThrow(() -> new ActorNotFoundException("Actor with ID: " + actorId + " not found"));
    }

    public Actor updateActor(UUID actorId, Actor incomingActor) {
        Actor existingActor = getActor(actorId);

        verifyActorUniqueness(incomingActor.getName());

        existingActor.setName(incomingActor.getName());
        existingActor.setBio(incomingActor.getBio());
        existingActor.setGender(incomingActor.getGender());

        return actorRepository.save(existingActor);
    }

    public Page<Actor> getActors(Integer page, Integer limit, String name) {
        Sort sort = Sort.by(Actor_.NAME).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return actorRepository.findAll(pageable);
        }

        return actorRepository.findAllByName(name, pageable);
    }

}
