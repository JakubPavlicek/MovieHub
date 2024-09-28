package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieCrewService {

    private final DirectorService directorService;
    private final MovieCastService castService;
    private final ActorService actorService;

    public Director getSavedDirector(Director director) {
        return directorService.getSavedDirector(director);
    }

    public List<MovieCast> getSavedMovieCasts(List<MovieCast> cast, Movie savedMovie) {
        return castService.getSavedMovieCasts(cast, savedMovie);
    }

    public void deleteAllMovieCastsByMovie(Movie existingMovie) {
        castService.deleteAllMovieCastsByMovie(existingMovie);
    }

    public Director getDirector(String directorId) {
        return directorService.getDirector(directorId);
    }

    public Actor getActor(String actorId) {
        return actorService.getActor(actorId);
    }

}
