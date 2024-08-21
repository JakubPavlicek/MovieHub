package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Actor_;
import com.moviehub.entity.Gender;
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

@Service
@AllArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    private final GenderService genderService;

    @Transactional
    public Actor getSavedActor(Actor actor) {
        return actorRepository.findByName(actor.getName())
                              .orElseGet(() -> saveActor(actor));
    }

    @Transactional
    public Actor addActor(Actor actor) {
        verifyActorUniqueness(actor);

        return saveActor(actor);
    }

    private void verifyActorUniqueness(Actor actor) {
        if (actorRepository.existsByName(actor.getName())) {
            throw new ActorAlreadyExistsException("Actor with name: " + actor.getName() + " already exists");
        }
    }

    private Actor saveActor(Actor actor) {
        Gender savedGender = genderService.getSavedGender(actor.getGender());
        actor.setGender(savedGender);

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor getActor(String actorId) {
        return actorRepository.findById(actorId)
                              .orElseThrow(() -> new ActorNotFoundException("Actor with ID: " + actorId + " not found"));
    }

    @Transactional
    public Actor updateActor(String actorId, Actor incomingActor) {
        Actor existingActor = getActor(actorId);

        if (incomingActor.getName() != null) {
            verifyActorUniqueness(incomingActor);
            existingActor.setName(incomingActor.getName());
        }
        if (incomingActor.getBio() != null) {
            existingActor.setBio(incomingActor.getBio());
        }
        if (incomingActor.getGender() != null) {
            Gender savedGender = genderService.getSavedGender(incomingActor.getGender());
            existingActor.setGender(savedGender);
        }

        return actorRepository.save(existingActor);
    }

    @Transactional
    public Page<Actor> getActors(Integer page, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, Actor_.NAME);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return actorRepository.findAll(pageable);
    }

}
