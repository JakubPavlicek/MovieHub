package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieCrewServiceTest {

    @Mock
    private DirectorService directorService;

    @Mock
    private MovieCastService castService;

    @Mock
    private ActorService actorService;

    @InjectMocks
    private MovieCrewService movieCrewService;

    private static final UUID DIRECTOR_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID ACTOR_ID = UUID.fromString("456e7890-e89b-12d3-a456-426614174001");
    private static final UUID MOVIE_ID = UUID.fromString("789e0123-e89b-12d3-a456-426614174002");
    private static final String DIRECTOR_NAME = "Jane Smith";
    private static final String ACTOR_NAME = "John Doe";

    @Test
    void shouldGetSavedDirector() {
        Director director = createDirector();

        when(directorService.getSavedDirector(director)).thenReturn(director);

        Director savedDirector = movieCrewService.getSavedDirector(director);

        assertThat(savedDirector).isNotNull();
        assertThat(savedDirector.getId()).isEqualTo(DIRECTOR_ID);
        assertThat(savedDirector.getName()).isEqualTo(DIRECTOR_NAME);
    }

    @Test
    void shouldGetSavedMovieCasts() {
        Movie movie = createMovie();
        Actor actor = createActor();
        MovieCast movieCast = createMovieCast(actor, movie);

        when(castService.getSavedMovieCasts(List.of(movieCast), movie)).thenReturn(List.of(movieCast));

        List<MovieCast> savedMovieCasts = movieCrewService.getSavedMovieCasts(List.of(movieCast), movie);

        assertThat(savedMovieCasts).hasSize(1);
        assertThat(savedMovieCasts.getFirst()
                                  .getActor()
                                  .getName()).isEqualTo(ACTOR_NAME);
    }

    @Test
    void shouldDeleteAllMovieCastsByMovie() {
        Movie movie = createMovie();

        movieCrewService.deleteAllMovieCastsByMovie(movie);

        verify(castService, times(1)).deleteAllMovieCastsByMovie(movie);
    }

    @Test
    void shouldGetDirectorById() {
        Director director = createDirector();

        when(directorService.getDirector(DIRECTOR_ID)).thenReturn(director);

        Director retrievedDirector = movieCrewService.getDirector(DIRECTOR_ID);

        assertThat(retrievedDirector).isNotNull();
        assertThat(retrievedDirector.getId()).isEqualTo(DIRECTOR_ID);
        assertThat(retrievedDirector.getName()).isEqualTo(DIRECTOR_NAME);
    }

    @Test
    void shouldGetActorById() {
        Actor actor = createActor();

        when(actorService.getActor(ACTOR_ID)).thenReturn(actor);

        Actor retrievedActor = movieCrewService.getActor(ACTOR_ID);

        assertThat(retrievedActor).isNotNull();
        assertThat(retrievedActor.getId()).isEqualTo(ACTOR_ID);
        assertThat(retrievedActor.getName()).isEqualTo(ACTOR_NAME);
    }

    private static Director createDirector() {
        return Director.builder()
                       .id(DIRECTOR_ID)
                       .name(DIRECTOR_NAME)
                       .build();
    }

    private static Movie createMovie() {
        return Movie.builder()
                    .id(MOVIE_ID)
                    .build();
    }

    private static Actor createActor() {
        return Actor.builder()
                    .id(ACTOR_ID)
                    .name(ACTOR_NAME)
                    .build();
    }

    private static MovieCast createMovieCast(Actor actor, Movie movie) {
        return MovieCast.builder()
                        .actor(actor)
                        .movie(movie)
                        .build();
    }

}