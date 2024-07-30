package com.movie_manager.controller;

import com.movie_manager.MoviesApi;
import com.movie_manager.dto.AddMovieRequest;
import com.movie_manager.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController implements MoviesApi {

    private final MovieService movieService;

    @Override
    public ResponseEntity<Void> addMovie(AddMovieRequest addMovieRequest) {
        movieService.addMovie(addMovieRequest);
        // TODO: zmenit response na 200 a vracet vytvoreny MovieResponse objekt
        return ResponseEntity.noContent().build();
    }

//    @Override
//    public ResponseEntity<Void> deleteMovie(String movieId) {
//        return movieService.deleteMovie(movieId);
//    }
//
//    @Override
//    public ResponseEntity<Movie> getMovie(String movieId) {
//        return movieService.getMovie(movieId);
//    }
//
//    @Override
//    public ResponseEntity<Movie> updateMovie(String movieId, UpdateMovieRequest updateMovieRequest) {
//        return movieService.updateMovie(movieId, updateMovieRequest);
//    }

}
