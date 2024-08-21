package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.repository.MovieCastRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieCastService {

    private final MovieCastRepository movieCastRepository;

    private final ActorService actorService;

    @Transactional
    public List<MovieCast> getSavedMovieCasts(List<MovieCast> movieCasts, Movie movie) {
        return movieCasts.stream()
                         .map(movieCast -> getSavedMovieCast(movieCast, movie))
                         .collect(Collectors.toCollection(ArrayList::new));
    }

    private MovieCast getSavedMovieCast(MovieCast movieCast, Movie movie) {
        Actor savedActor = actorService.getSavedActor(movieCast.getActor());
        movieCast.setActor(savedActor);
        movieCast.setMovie(movie);

        return movieCastRepository.save(movieCast);
    }

    @Transactional
    public void deleteAllMovieCastsByMovie(Movie movie) {
        movieCastRepository.deleteAllByMovie(movie);
    }

}
