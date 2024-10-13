package com.moviehub.mapper.dto;

import com.moviehub.dto.ActorDetailsResponse;
import com.moviehub.dto.ActorPage;
import com.moviehub.dto.AddActorRequest;
import com.moviehub.dto.GenderName;
import com.moviehub.dto.UpdateActorRequest;
import com.moviehub.entity.Actor;
import com.moviehub.entity.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ActorMapperTest {

    private static final String ACTOR_NAME = "Actor Name";
    private static final String ACTOR_BIO = "Actor Bio";
    private static final GenderName GENDER_NAME = GenderName.MALE;
    private static final UUID ACTOR_ID = UUID.fromString("0d47bec2-f80e-4969-be7c-ef42517e11ee");

    @Test
    void shouldMapAddActorRequestToActor() {
        AddActorRequest addActorRequest = buildAddActorRequest();

        Actor actor = ActorMapper.mapToActor(addActorRequest);

        assertThat(actor.getName()).isEqualTo(ACTOR_NAME);
        assertThat(actor.getBio()).isEqualTo(ACTOR_BIO);
        assertThat(actor.getGender()).isEqualTo(Gender.valueOf(GENDER_NAME.name()));
    }

    @Test
    void shouldMapUpdateActorRequestToActor() {
        UpdateActorRequest updateActorRequest = buildUpdateActorRequest();

        Actor actor = ActorMapper.mapToActor(updateActorRequest);

        assertThat(actor.getName()).isEqualTo(ACTOR_NAME);
        assertThat(actor.getBio()).isEqualTo(ACTOR_BIO);
        assertThat(actor.getGender()).isEqualTo(Gender.valueOf(GENDER_NAME.name()));
    }

    @Test
    void shouldMapActorToActorDetailsResponse() {
        Actor actor = buildActor(ACTOR_ID, ACTOR_NAME, ACTOR_BIO, Gender.MALE);

        ActorDetailsResponse actorDetailsResponse = ActorMapper.mapToActorDetailsResponse(actor);

        assertThat(actorDetailsResponse.getId()).isEqualTo(ACTOR_ID);
        assertThat(actorDetailsResponse.getName()).isEqualTo(ACTOR_NAME);
        assertThat(actorDetailsResponse.getBio()).isEqualTo(ACTOR_BIO);
        assertThat(actorDetailsResponse.getGender()).isEqualTo(GenderName.MALE);
    }

    @Test
    void shouldMapActorPageToActorPageResponse() {
        List<Actor> actors = List.of(
            buildActor(UUID.randomUUID(), "Actor 1", "Bio 1", Gender.MALE),
            buildActor(UUID.randomUUID(), "Actor 2", "Bio 2", Gender.FEMALE)
        );
        Page<Actor> actorPage = new PageImpl<>(actors, PageRequest.of(0, 10), actors.size());

        ActorPage actorPageResponse = ActorMapper.mapToActorPage(actorPage);

        assertThat(actorPageResponse.getContent()).hasSize(2);
        assertThat(actorPageResponse.getTotalElements()).isEqualTo(actors.size());
        assertThat(actorPageResponse.getTotalPages()).isEqualTo(1);
        assertThat(actorPageResponse.getNumber()).isZero();
        assertThat(actorPageResponse.getFirst()).isTrue();
        assertThat(actorPageResponse.getLast()).isTrue();
    }

    private AddActorRequest buildAddActorRequest() {
        return AddActorRequest.builder()
                              .name(ACTOR_NAME)
                              .bio(ACTOR_BIO)
                              .gender(GENDER_NAME)
                              .build();
    }

    private UpdateActorRequest buildUpdateActorRequest() {
        return UpdateActorRequest.builder()
                                 .name(ACTOR_NAME)
                                 .bio(ACTOR_BIO)
                                 .gender(GENDER_NAME)
                                 .build();
    }

    private Actor buildActor(UUID id, String name, String bio, Gender gender) {
        return Actor.builder()
                    .id(id)
                    .name(name)
                    .bio(bio)
                    .gender(gender)
                    .build();
    }

}