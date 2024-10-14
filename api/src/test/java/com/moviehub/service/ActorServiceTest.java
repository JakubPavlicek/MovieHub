package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Actor_;
import com.moviehub.entity.Gender;
import com.moviehub.exception.ActorAlreadyExistsException;
import com.moviehub.exception.ActorNotFoundException;
import com.moviehub.repository.ActorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

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

    private static final UUID ACTOR_ID = UUID.fromString("9f50cdd6-be99-4477-8872-f71d97ab7fc2");
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10, Sort.by(Actor_.NAME).ascending());
    
    private static final String FIRST_ACTOR_NAME = "Arnold";
    private static final String FIRST_ACTOR_BIO = "Bio 1";
    private static final Gender FIRST_ACTOR_GENDER = Gender.MALE;

    private static final String SECOND_ACTOR_NAME = "Kristy";
    private static final String SECOND_ACTOR_BIO = "Bio 2";
    private static final Gender SECOND_ACTOR_GENDER = Gender.FEMALE;

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
    void shouldThrowActorAlreadyExistsExceptionWhenActorAlreadyExists() {
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
        when(actorRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ActorNotFoundException.class).isThrownBy(() -> actorService.getActor(ACTOR_ID));
    }

    private static Stream<Arguments> provideActorsForUpdateTest() {
        return Stream.of(
            Arguments.of(SECOND_ACTOR_NAME, SECOND_ACTOR_BIO, SECOND_ACTOR_GENDER, createActor(SECOND_ACTOR_NAME, SECOND_ACTOR_BIO, SECOND_ACTOR_GENDER)),
            Arguments.of(null, SECOND_ACTOR_BIO, SECOND_ACTOR_GENDER, createActor(FIRST_ACTOR_NAME, SECOND_ACTOR_BIO, SECOND_ACTOR_GENDER)),
            Arguments.of(SECOND_ACTOR_NAME, null, SECOND_ACTOR_GENDER, createActor(SECOND_ACTOR_NAME, FIRST_ACTOR_BIO, SECOND_ACTOR_GENDER)),
            Arguments.of(SECOND_ACTOR_NAME, SECOND_ACTOR_BIO, null, createActor(SECOND_ACTOR_NAME, SECOND_ACTOR_BIO, FIRST_ACTOR_GENDER))
        );
    }

    @ParameterizedTest
    @MethodSource("provideActorsForUpdateTest")
    void shouldUpdateActorFields(String name, String bio, Gender gender, Actor expectedActor) {
        Actor actor = createActor(FIRST_ACTOR_NAME, FIRST_ACTOR_BIO, FIRST_ACTOR_GENDER);

        when(actorRepository.findById(actor.getId())).thenReturn(Optional.of(actor));
        if (name != null) {
            when(actorRepository.existsByName(name)).thenReturn(false);
        }
        when(actorRepository.save(actor)).thenReturn(expectedActor);

        Actor updatedActor = actorService.updateActor(actor.getId(), createActor(name, bio, gender));

        assertThat(updatedActor.getName()).isEqualTo(expectedActor.getName());
        assertThat(updatedActor.getBio()).isEqualTo(expectedActor.getBio());
        assertThat(updatedActor.getGender()).isEqualTo(expectedActor.getGender());
    }

    @Test
    void shouldGetActorsWithName() {
        Actor actor = createActor();
        Page<Actor> actorPage = new PageImpl<>(List.of(actor), PAGE_REQUEST, 1L);

        when(actorRepository.findAllByName(actor.getName(), PAGE_REQUEST)).thenReturn(actorPage);

        Page<Actor> actors = actorService.getActors(PAGE_REQUEST.getPageNumber(), PAGE_REQUEST.getPageSize(), actor.getName());

        assertThat(actors.getTotalElements()).isEqualTo(1);
        assertThat(actors.getContent()
                         .getFirst()
                         .getName()).isEqualTo(actor.getName());
    }

    @Test
    void shouldGetActorsWhenNameIsEmpty() {
        String name = "";
        Actor actor = createActor();
        Page<Actor> actorPage = new PageImpl<>(List.of(actor), PAGE_REQUEST, 1L);

        when(actorRepository.findAll(PAGE_REQUEST)).thenReturn(actorPage);

        Page<Actor> actors = actorService.getActors(PAGE_REQUEST.getPageNumber(), PAGE_REQUEST.getPageSize(), name);

        assertThat(actors.getTotalElements()).isEqualTo(1);
        assertThat(actors.getContent()
                         .getFirst()
                         .getName()).isEqualTo(actor.getName());
    }

    private static Actor createActor() {
        return Actor.builder()
                    .id(ACTOR_ID)
                    .name(FIRST_ACTOR_NAME)
                    .build();
    }

    private static Actor createActor(String name, String bio, Gender gender) {
        return Actor.builder()
                    .id(ACTOR_ID)
                    .name(name)
                    .bio(bio)
                    .gender(gender)
                    .build();
    }

}