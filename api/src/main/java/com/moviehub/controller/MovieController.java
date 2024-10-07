package com.moviehub.controller;

import com.moviehub.MoviesApi;
import com.moviehub.dto.AddCommentRequest;
import com.moviehub.dto.AddMovieRequest;
import com.moviehub.dto.CommentPage;
import com.moviehub.dto.MovieDetailsResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.MovieUserRating;
import com.moviehub.dto.UpdateMovieRequest;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import com.moviehub.mapper.dto.CommentMapper;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.mapper.dto.MovieRatingMapper;
import com.moviehub.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
    public ResponseEntity<Void> deleteMovie(UUID movieId) {
        movieService.deleteMovie(movieId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MovieDetailsResponse> getMovie(UUID movieId) {
        Movie movie = movieService.getMovie(movieId);
        MovieDetailsResponse movieDetailsResponse = MovieMapper.mapToMovieDetailsResponse(movie);

        return ResponseEntity.ok(movieDetailsResponse);
    }

    @Override
    public ResponseEntity<MovieDetailsResponse> updateMovie(UUID movieId, UpdateMovieRequest updateMovieRequest) {
        Movie movie = MovieMapper.mapToMovie(updateMovieRequest);
        movie = movieService.updateMovie(movieId, movie);
        MovieDetailsResponse movieResponse = MovieMapper.mapToMovieDetailsResponse(movie);

        return ResponseEntity.ok(movieResponse);
    }

    @Override
    public ResponseEntity<MoviePage> listMovies(Integer page, Integer limit, String sort) {
        Page<Movie> movies = movieService.listMovies(page, limit, sort);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

    @Override
    public ResponseEntity<MoviePage> filterMovies(Integer page, Integer limit, String sort, String releaseYear, String genre, String country) {
        Page<Movie> movies = movieService.filterMovies(page, limit, sort, releaseYear, genre, country);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

    @Override
    public ResponseEntity<MoviePage> searchMovies(Integer page, Integer limit, String sort, String keyword) {
        Page<Movie> movies = movieService.searchMovies(page, limit, sort, keyword);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

    @Override
    public ResponseEntity<Void> addComment(UUID movieId, AddCommentRequest addCommentRequest) {
        Comment comment = CommentMapper.mapToComment(addCommentRequest);
        movieService.addComment(movieId, comment, addCommentRequest.getParentCommentId());

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CommentPage> getComments(UUID movieId, Integer page, Integer limit, String sort) {
        Page<Comment> comments = movieService.getComments(movieId, page, limit, sort);
        CommentPage commentPage = CommentMapper.mapToCommentPage(comments);

        return ResponseEntity.ok(commentPage);
    }

    @Override
    public ResponseEntity<Void> addRating(UUID movieId, MovieUserRating rating) {
        movieService.addRating(movieId, rating.getRating());

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MovieUserRating> getUserMovieRating(UUID movieId) {
        MovieRating movieRating = movieService.getRating(movieId);
        MovieUserRating rating = MovieRatingMapper.mapToRating(movieRating);

        return ResponseEntity.ok(rating);
    }

}
