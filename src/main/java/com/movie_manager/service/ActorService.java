package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.repository.ActorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    @Transactional
    public Actor getSavedActor(Actor actor) {
        return actorRepository.findByName(actor.getName())
                              .orElseGet(() -> actorRepository.save(actor));
    }

}
