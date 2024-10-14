package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Actor_;
import com.moviehub.entity.Gender;
import com.moviehub.exception.ActorAlreadyExistsException;
import com.moviehub.exception.ActorNotFoundException;
import com.moviehub.repository.ActorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    @Test
    void shouldGetSavedActorWhenActorExists() {
        Actor actor = createActor();

        when(actorRepository.findByName(actor.getName())).thenReturn(Optional.of(actor));

        Actor savedActor = actorService.getSavedActor(actor);

        assertThat(savedActor.getName()).isEqualTo(actor.getName());
    }

    @Test
    void shouldSaveActorWhenActorDoesNotExist() {
        Actor actor = createActor();

        when(actorRepository.findByName(actor.getName())).thenReturn(Optional.empty());
        when(actorRepository.save(actor)).thenReturn(actor);

        Actor savedActor = actorService.getSavedActor(actor);

        assertThat(savedActor.getName()).isEqualTo(actor.getName());
    }

    @Test
    void shouldAddActorIfActorDoesNotExist() {
        Actor actor = createActor();

        when(actorRepository.existsByName(actor.getName())).thenReturn(false);
        when(actorRepository.save(actor)).thenReturn(actor);

        Actor addedActor = actorService.addActor(actor);

        assertThat(addedActor.getName()).isEqualTo(actor.getName());
    }

    @Test
    void shouldThrowActorAlreadyExistsExceptionWhenUserAlreadyExists() {
        Actor actor = createActor();

        when(actorRepository.existsByName(actor.getName())).thenReturn(true);

        assertThatExceptionOfType(ActorAlreadyExistsException.class).isThrownBy(() -> actorService.addActor(actor));
    }

    @Test
    void shouldGetActorWhenActorExists() {
        Actor actor = createActor();

        when(actorRepository.findById(actor.getId())).thenReturn(Optional.of(actor));

        Actor savedActor = actorService.getActor(actor.getId());

        assertThat(savedActor.getName()).isEqualTo(actor.getName());
    }

    @Test
    void shouldThrowActorNotFoundExceptionWhenActorDoesNotExist() {
        UUID actorId = UUID.fromString("5759868d-3063-42ec-95aa-ba259d9e52de");

        when(actorRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ActorNotFoundException.class).isThrownBy(() -> actorService.getActor(actorId));
    }

    @Test
    void shouldUpdateActorWhenIncomingActorIsValid() {
        Actor actor = createActor("Arnold", "Bio 1", Gender.MALE);
        Actor incomingActor = createActor("Kristy", "Bio 2", Gender.FEMALE);

        when(actorRepository.findById(actor.getId())).thenReturn(Optional.of(actor));
        when(actorRepository.existsByName(incomingActor.getName())).thenReturn(false);
        when(actorRepository.save(actor)).thenReturn(incomingActor);

        Actor updatedActor = actorService.updateActor(actor.getId(), incomingActor);

        assertThat(updatedActor.getName()).isEqualTo(incomingActor.getName());
        assertThat(updatedActor.getBio()).isEqualTo(incomingActor.getBio());
        assertThat(updatedActor.getGender()).isEqualTo(incomingActor.getGender());
    }

    @Test
    void shouldUpdateActorBioAndGenderWhenIncomingActorNameIsNull() {
        Actor actor = createActor("Arnold", "Bio 1", Gender.MALE);
        Actor incomingActor = createActor(null, "Bio 2", Gender.FEMALE);
        Actor expectedActor = createActor(actor.getName(), incomingActor.getBio(), incomingActor.getGender());

        when(actorRepository.findById(actor.getId())).thenReturn(Optional.of(actor));
        when(actorRepository.save(actor)).thenReturn(expectedActor);

        Actor updatedActor = actorService.updateActor(actor.getId(), incomingActor);

        assertThat(updatedActor.getName()).isEqualTo(expectedActor.getName());
        assertThat(updatedActor.getBio()).isEqualTo(expectedActor.getBio());
        assertThat(updatedActor.getGender()).isEqualTo(expectedActor.getGender());
    }

    @Test
    void shouldUpdateActorNameAndGenderWhenIncomingActorBioIsNull() {
        Actor actor = createActor("Arnold", "Bio 1", Gender.MALE);
        Actor incomingActor = createActor("Kristy", null, Gender.FEMALE);
        Actor expectedActor = createActor(incomingActor.getName(), actor.getBio(), incomingActor.getGender());

        when(actorRepository.findById(actor.getId())).thenReturn(Optional.of(actor));
        when(actorRepository.existsByName(incomingActor.getName())).thenReturn(false);
        when(actorRepository.save(actor)).thenReturn(expectedActor);

        Actor updatedActor = actorService.updateActor(actor.getId(), incomingActor);

        assertThat(updatedActor.getName()).isEqualTo(expectedActor.getName());
        assertThat(updatedActor.getBio()).isEqualTo(expectedActor.getBio());
        assertThat(updatedActor.getGender()).isEqualTo(expectedActor.getGender());
    }

    @Test
    void shouldUpdateActorNameAndBioWhenIncomingActorGenderIsNull() {
        Actor actor = createActor("Arnold", "Bio 1", Gender.MALE);
        Actor incomingActor = createActor("Kristy", "Bio 2", null);
        Actor expectedActor = createActor(incomingActor.getName(), incomingActor.getBio(), actor.getGender());

        when(actorRepository.findById(actor.getId())).thenReturn(Optional.of(actor));
        when(actorRepository.existsByName(incomingActor.getName())).thenReturn(false);
        when(actorRepository.save(actor)).thenReturn(expectedActor);

        Actor updatedActor = actorService.updateActor(actor.getId(), incomingActor);

        assertThat(updatedActor.getName()).isEqualTo(expectedActor.getName());
        assertThat(updatedActor.getBio()).isEqualTo(expectedActor.getBio());
        assertThat(updatedActor.getGender()).isEqualTo(expectedActor.getGender());
    }

    @Test
    void shouldGetActorsWithName() {
        String name = "Arnold";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Actor_.NAME).ascending());

        Page<Actor> actorPage = new PageImpl<>(List.of(createActor()), pageRequest, 1);

        when(actorRepository.findAllByName(name, pageRequest)).thenReturn(actorPage);

        Page<Actor> actors = actorService.getActors(pageRequest.getPageNumber(), pageRequest.getPageSize(), name);

        assertThat(actors.getTotalElements()).isEqualTo(1);
    }

    private Actor createActor() {
        return Actor.builder()
                    .id(UUID.fromString("9f50cdd6-be99-4477-8872-f71d97ab7fc2"))
                    .name("Arnold")
                    .build();
    }

    private Actor createActor(String name, String bio, Gender gender) {
        return Actor.builder()
                    .id(UUID.fromString("9f50cdd6-be99-4477-8872-f71d97ab7fc2"))
                    .name(name)
                    .bio(bio)
                    .gender(gender)
                    .build();
    }

}