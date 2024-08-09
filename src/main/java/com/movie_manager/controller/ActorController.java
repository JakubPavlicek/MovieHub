package com.movie_manager.controller;

import com.movie_manager.ActorsApi;
import com.movie_manager.dto.ActorDetailsResponse;
import com.movie_manager.dto.ActorPage;
import com.movie_manager.dto.AddActorRequest;
import com.movie_manager.dto.MoviePage;
import com.movie_manager.dto.UpdateActorRequest;
import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Movie;
import com.movie_manager.mapper.dto.ActorMapper;
import com.movie_manager.mapper.dto.MovieMapper;
import com.movie_manager.service.ActorService;
import com.movie_manager.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActorController implements ActorsApi {

    private final ActorService actorService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<ActorDetailsResponse> addActor(AddActorRequest addActorRequest) {
        Actor actor = ActorMapper.mapToActor(addActorRequest);
        actor = actorService.addActor(actor);
        ActorDetailsResponse actorDetailsResponse = ActorMapper.mapToActorDetailsResponse(actor);

        return ResponseEntity.status(HttpStatus.CREATED).body(actorDetailsResponse);
    }

    @Override
    public ResponseEntity<ActorDetailsResponse> getActorById(String actorId) {
        Actor actor = actorService.getActor(actorId);
        ActorDetailsResponse actorDetailsResponse = ActorMapper.mapToActorDetailsResponse(actor);

        return ResponseEntity.ok(actorDetailsResponse);
    }

    @Override
    public ResponseEntity<ActorPage> getActors(Integer page, Integer limit) {
        Page<Actor> actors = actorService.getActors(page, limit);
        ActorPage actorPage = ActorMapper.mapToActorPage(actors);

        return ResponseEntity.ok(actorPage);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithActor(String actorId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithActor(actorId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

    @Override
    public ResponseEntity<ActorDetailsResponse> updateActor(String actorId, UpdateActorRequest updateActorRequest) {
        Actor actor = ActorMapper.mapToActor(updateActorRequest);
        actor = actorService.updateActor(actorId, actor);
        ActorDetailsResponse actorDetailsResponse = ActorMapper.mapToActorDetailsResponse(actor);

        return ResponseEntity.ok(actorDetailsResponse);
    }

}
