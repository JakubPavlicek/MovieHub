package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.repository.ActorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    @Transactional
    public List<Actor> getExistingActors(List<Actor> actors) {
        List<Actor> existingActors = new ArrayList<>();

        actors.forEach(actor -> {
            Actor existingActor = actorRepository.findByName(actor.getName())
                                                 .orElseGet(() -> actorRepository.save(actor));
            existingActors.add(existingActor);
        });

        return existingActors;
    }

}
