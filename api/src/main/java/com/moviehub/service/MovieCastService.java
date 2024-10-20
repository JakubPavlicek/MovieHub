package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.repository.MovieCastRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MovieCastService {

    private final MovieCastRepository movieCastRepository;

    private final ActorService actorService;

    public List<MovieCast> getSavedMovieCasts(List<MovieCast> movieCasts, Movie movie) {
        log.info("retrieving saved countries");

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

    public void deleteAllMovieCastsByMovie(Movie movie) {
        movieCastRepository.deleteAllByMovie(movie);
    }

}
