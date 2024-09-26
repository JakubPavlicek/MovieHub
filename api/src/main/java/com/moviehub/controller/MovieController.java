package com.moviehub.controller;

import com.moviehub.MoviesApi;
import com.moviehub.dto.AddMovieRequest;
import com.moviehub.dto.MovieDetailsResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.UpdateMovieRequest;
import com.moviehub.entity.Movie;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController implements MoviesApi {

    private final MovieService movieService;

    @Override
    public ResponseEntity<MovieDetailsResponse> addMovie(AddMovieRequest addMovieRequest) {
        Movie movie = MovieMapper.mapToMovie(addMovieRequest);
        movie = movieService.addMovie(movie);
        MovieDetailsResponse movieResponse = MovieMapper.mapToMovieDetailsResponse(movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(movieResponse);
    }

    @Override
    public ResponseEntity<Void> deleteMovie(String movieId) {
        movieService.deleteMovie(movieId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MovieDetailsResponse> getMovie(String movieId) {
        Movie movie = movieService.getMovie(movieId);
        MovieDetailsResponse movieDetailsResponse = MovieMapper.mapToMovieDetailsResponse(movie);

        return ResponseEntity.ok(movieDetailsResponse);
    }

    @Override
    public ResponseEntity<MovieDetailsResponse> updateMovie(String movieId, UpdateMovieRequest updateMovieRequest) {
        Movie movie = MovieMapper.mapToMovie(updateMovieRequest);
        movie = movieService.updateMovie(movieId, movie);
        MovieDetailsResponse movieResponse = MovieMapper.mapToMovieDetailsResponse(movie);

        return ResponseEntity.ok(movieResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMovies(Integer page, Integer limit, String sort, String name, String releaseDate, String duration, String description, String director, List<String> actors, List<String> genres, List<String> countries, String keyword) {
        Page<Movie> movies = movieService.getMovies(page, limit, sort, name, releaseDate, duration, description, director, actors, genres, countries, keyword);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
