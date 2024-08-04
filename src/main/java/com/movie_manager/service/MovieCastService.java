package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.MovieCast;
import com.movie_manager.repository.MovieCastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieCastService {

    private final MovieCastRepository movieCastRepository;

    private final ActorService actorService;

    public List<MovieCast> getSavedMovieCasts(List<MovieCast> movieCasts, Movie movie) {
        List<MovieCast> existingMovieCasts = new ArrayList<>();

        for (MovieCast movieCast : movieCasts) {
            Actor savedActor = actorService.getSavedActor(movieCast.getActor());
            movieCast.setActor(savedActor);
            movieCast.setMovie(movie);

            MovieCast savedMovieCast = movieCastRepository.save(movieCast);
            existingMovieCasts.add(savedMovieCast);
        }

        return existingMovieCasts;
    }

}
