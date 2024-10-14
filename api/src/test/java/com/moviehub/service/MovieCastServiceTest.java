package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.repository.MovieCastRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieCastServiceTest {

    @Mock
    private MovieCastRepository movieCastRepository;

    @Mock
    private ActorService actorService;

    @InjectMocks
    private MovieCastService movieCastService;

    private static final UUID MOVIE_ID = UUID.fromString("1e9a5dce-e935-48ea-9a5f-220d5246bc77");
    private static final UUID ACTOR_ID = UUID.fromString("bd5b8922-6e43-4e78-b2a2-3b41526c3f67");
    private static final UUID MOVIE_CAST_ID = UUID.fromString("88e4c60e-65f5-4ab8-8c4f-04ea7127d7fc");
    private static final String ACTOR_NAME = "John Doe";

    @Test
    void shouldGetSavedMovieCasts() {
        Movie movie = createMovie();
        Actor actor = createActor();
        MovieCast movieCast = createMovieCast(actor, movie);

        when(actorService.getSavedActor(actor)).thenReturn(actor);
        when(movieCastRepository.save(any(MovieCast.class))).thenReturn(movieCast);

        List<MovieCast> movieCasts = List.of(movieCast);
        List<MovieCast> savedMovieCasts = movieCastService.getSavedMovieCasts(movieCasts, movie);

        assertThat(savedMovieCasts).hasSize(1);
        assertThat(savedMovieCasts.getFirst()
                                  .getActor()
                                  .getName()).isEqualTo(ACTOR_NAME);
        assertThat(savedMovieCasts.getFirst()
                                  .getMovie()
                                  .getId()).isEqualTo(MOVIE_ID);
    }

    @Test
    void shouldDeleteAllMovieCastsByMovie() {
        Movie movie = createMovie();

        movieCastService.deleteAllMovieCastsByMovie(movie);

        verify(movieCastRepository, times(1)).deleteAllByMovie(movie);
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
                        .id(MOVIE_CAST_ID)
                        .actor(actor)
                        .movie(movie)
                        .build();
    }

}