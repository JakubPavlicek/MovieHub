package com.movie_manager.controller;

import com.movie_manager.MoviesApi;
import com.movie_manager.dto.AddMovieRequest;
import com.movie_manager.dto.MovieResponse;
import com.movie_manager.dto.UpdateMovieRequest;
import com.movie_manager.entity.Movie;
import com.movie_manager.mapper.MovieMapper;
import com.movie_manager.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController implements MoviesApi {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Override
    public ResponseEntity<MovieResponse> addMovie(AddMovieRequest addMovieRequest) {
        Movie movie = movieMapper.map(addMovieRequest);
        movie = movieService.addMovie(movie);
        MovieResponse movieResponse = movieMapper.map(movie);

        return ResponseEntity.ok(movieResponse);
    }

    @Override
    public ResponseEntity<Void> deleteMovie(String movieId) {
        movieService.deleteMovie(movieId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MovieResponse> getMovie(String movieId) {
        Movie movie = movieService.getMovie(movieId);
        MovieResponse movieResponse = movieMapper.map(movie);

        return ResponseEntity.ok(movieResponse);
    }

    @Override
    public ResponseEntity<MovieResponse> updateMovie(String movieId, UpdateMovieRequest updateMovieRequest) {
        Movie movie = movieMapper.map(updateMovieRequest);
        movie = movieService.updateMovie(movieId, movie);
        MovieResponse movieResponse = movieMapper.map(movie);

        return ResponseEntity.ok(movieResponse);
    }

}