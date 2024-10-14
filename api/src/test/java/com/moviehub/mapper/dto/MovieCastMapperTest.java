package com.moviehub.mapper.dto;

import com.moviehub.dto.MovieCastDetailsResponse;
import com.moviehub.dto.MovieCastRequest;
import com.moviehub.entity.Actor;
import com.moviehub.entity.MovieCast;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MovieCastMapperTest {

    private static final String ACTOR_HERO = "Actor Hero";
    private static final String ACTOR_VILLAIN = "Actor Villain";

    private static final String ROLE_HERO = "Hero";
    private static final String ROLE_VILLAIN = "Villain";

    private static final UUID FIRST_ACTOR_ID = UUID.fromString("f3fd951c-8c54-415b-a958-b9174a772bf5");
    private static final UUID SECOND_ACTOR_ID = UUID.fromString("197109d7-d821-4461-ae5d-08f0fdf677a8");

    @Test
    void shouldMapMovieCastRequestsToMovieCasts() {
        List<MovieCastRequest> castRequests = List.of(
            createMovieCastRequest(ACTOR_HERO, ROLE_HERO),
            createMovieCastRequest(ACTOR_VILLAIN, ROLE_VILLAIN)
        );

        List<MovieCast> movieCasts = MovieCastMapper.mapToMovieCasts(castRequests);

        assertThat(movieCasts).hasSize(2);

        MovieCast movieCast1 = movieCasts.getFirst();
        assertThat(movieCast1.getActor().getName()).isEqualTo(ACTOR_HERO);
        assertThat(movieCast1.getRole()).isEqualTo(ROLE_HERO);

        MovieCast movieCast2 = movieCasts.get(1);
        assertThat(movieCast2.getActor().getName()).isEqualTo(ACTOR_VILLAIN);
        assertThat(movieCast2.getRole()).isEqualTo(ROLE_VILLAIN);
    }

    @Test
    void shouldMapMovieCastToMovieCastDetailsResponse() {
        List<MovieCast> movieCasts = List.of(
            createMovieCast(FIRST_ACTOR_ID, ACTOR_HERO, ROLE_HERO),
            createMovieCast(SECOND_ACTOR_ID, ACTOR_VILLAIN, ROLE_VILLAIN)
        );

        List<MovieCastDetailsResponse> castDetailsResponses = MovieCastMapper.mapToMovieCastDetailsResponse(movieCasts);

        assertThat(castDetailsResponses).hasSize(2);

        MovieCastDetailsResponse response1 = castDetailsResponses.getFirst();
        assertThat(response1.getId()).isEqualTo(FIRST_ACTOR_ID);
        assertThat(response1.getName()).isEqualTo(ACTOR_HERO);
        assertThat(response1.getRole()).isEqualTo(ROLE_HERO);

        MovieCastDetailsResponse response2 = castDetailsResponses.get(1);
        assertThat(response2.getId()).isEqualTo(SECOND_ACTOR_ID);
        assertThat(response2.getName()).isEqualTo(ACTOR_VILLAIN);
        assertThat(response2.getRole()).isEqualTo(ROLE_VILLAIN);
    }

    @Test
    void shouldHandleEmptyMovieCastRequests() {
        List<MovieCast> movieCasts = MovieCastMapper.mapToMovieCasts(List.of());

        assertThat(movieCasts).isEmpty();
    }

    @Test
    void shouldHandleEmptyMovieCasts() {
        List<MovieCastDetailsResponse> castDetailsResponses = MovieCastMapper.mapToMovieCastDetailsResponse(List.of());

        assertThat(castDetailsResponses).isEmpty();
    }

    private static MovieCastRequest createMovieCastRequest(String name, String role) {
        return MovieCastRequest.builder()
                               .name(name)
                               .role(role)
                               .build();
    }

    private static MovieCast createMovieCast(UUID actorId, String actorName, String role) {
        Actor actor = Actor.builder()
                           .id(actorId)
                           .name(actorName)
                           .build();

        return MovieCast.builder()
                        .actor(actor)
                        .role(role)
                        .build();
    }

}