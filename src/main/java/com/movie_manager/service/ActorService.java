package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Actor_;
import com.movie_manager.entity.Gender;
import com.movie_manager.exception.ActorNotFoundException;
import com.movie_manager.repository.ActorRepository;
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
                              .orElseGet(() -> addActor(actor));
    }

    @Transactional
    public Actor addActor(Actor actor) {
        Gender savedGender = genderService.getSavedGender(actor.getGender());
        actor.setGender(savedGender);

        return actorRepository.save(actor);
    }

    public Actor getActor(String actorId) {
        return actorRepository.findById(actorId)
                              .orElseThrow(() -> new ActorNotFoundException("Actor with ID: " + actorId + " not found"));
    }

    public Actor updateActor(String actorId, Actor incomingActor) {
        Actor existingActor = getActor(actorId);

        if (incomingActor.getName() != null) {
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

    public Page<Actor> getActors(Integer page, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, Actor_.NAME);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return actorRepository.findAll(pageable);
    }

}
