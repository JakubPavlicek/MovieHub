package com.moviehub.controller;

import com.moviehub.ActorsApi;
import com.moviehub.dto.ActorDetailsResponse;
import com.moviehub.dto.ActorPage;
import com.moviehub.dto.AddActorRequest;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.UpdateActorRequest;
import com.moviehub.entity.Actor;
import com.moviehub.entity.Movie;
import com.moviehub.mapper.dto.ActorMapper;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.service.ActorService;
import com.moviehub.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Controller class for managing actors.
/// It provides endpoints to add, retrieve, update actors and get movies associated with a specific actor.
@RestController
@RequiredArgsConstructor
public class ActorController implements ActorsApi {

    /// Service for handling actor-related operations.
    private final ActorService actorService;
    /// Service for handling movie-related operations.
    private final MovieService movieService;

    @Override
    public ResponseEntity<ActorDetailsResponse> addActor(AddActorRequest addActorRequest) {
        Actor actor = ActorMapper.mapToActor(addActorRequest);
        actor = actorService.addActor(actor);
        ActorDetailsResponse actorDetailsResponse = ActorMapper.mapToActorDetailsResponse(actor);

        return ResponseEntity.status(HttpStatus.CREATED).body(actorDetailsResponse);
    }

    @Override
    public ResponseEntity<ActorPage> getActors(Integer page, Integer limit, String name) {
        Page<Actor> actors = actorService.getActors(page, limit, name);
        ActorPage actorPage = ActorMapper.mapToActorPage(actors);

        return ResponseEntity.ok(actorPage);
    }

    @Override
    public ResponseEntity<ActorDetailsResponse> getActor(UUID actorId) {
        Actor actor = actorService.getActor(actorId);
        ActorDetailsResponse actorDetailsResponse = ActorMapper.mapToActorDetailsResponse(actor);

        return ResponseEntity.ok(actorDetailsResponse);
    }

    @Override
    public ResponseEntity<ActorDetailsResponse> updateActor(UUID actorId, UpdateActorRequest updateActorRequest) {
        Actor actor = ActorMapper.mapToActor(updateActorRequest);
        actor = actorService.updateActor(actorId, actor);
        ActorDetailsResponse actorDetailsResponse = ActorMapper.mapToActorDetailsResponse(actor);

        return ResponseEntity.ok(actorDetailsResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithActor(UUID actorId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithActor(actorId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
