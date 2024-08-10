package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.MovieCast;
import com.movie_manager.repository.MovieCastRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieCastService {

    private final MovieCastRepository movieCastRepository;

    private final ActorService actorService;

    @Transactional
    public Set<MovieCast> getSavedMovieCasts(Set<MovieCast> movieCasts, Movie movie) {
        return movieCasts.stream()
                         .map(movieCast -> getSavedMovieCast(movieCast, movie))
                         .collect(Collectors.toCollection(HashSet::new));
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

    @Transactional
    public Page<Movie> findMoviesByActor(String actorId, Pageable pageable) {
        Actor actor = actorService.getActor(actorId);

        return movieCastRepository.findMoviesByActor(actor, pageable);
    }

}
