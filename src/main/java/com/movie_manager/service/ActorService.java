package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Gender;
import com.movie_manager.repository.ActorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    private final GenderService genderService;

    @Transactional
    public Actor getSavedActor(Actor actor) {
        return actorRepository.findByName(actor.getName())
                              .orElseGet(() -> {
                                  Gender savedGender = genderService.getSavedGender(actor.getGender());
                                  actor.setGender(savedGender);
                                  return actorRepository.save(actor);
                              });
    }

}
