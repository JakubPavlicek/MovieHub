package com.moviehub.controller;

import com.moviehub.MoviesApi;
import com.moviehub.dto.AddCommentRequest;
import com.moviehub.dto.AddMovieRequest;
import com.moviehub.dto.CommentInfoPage;
import com.moviehub.dto.MovieDetailsResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.MovieUserRating;
import com.moviehub.dto.UpdateMovieRequest;
import com.moviehub.dto.YearListResponse;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.mapper.dto.CommentInfoMapper;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Controller class for managing movies.
/// It provides endpoints for performing operations related to movies.
@RestController
@RequiredArgsConstructor
public class MovieController implements MoviesApi {

    /// Service for handling operations related to movies.
    private final MovieService movieService;

    @Override
    public ResponseEntity<MovieDetailsResponse> addMovie(AddMovieRequest addMovieRequest) {
        Movie movie = MovieMapper.mapToMovie(addMovieRequest);
        movie = movieService.addMovie(movie);
        MovieDetailsResponse movieResponse = MovieMapper.mapToMovieDetailsResponse(movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(movieResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMovies(Integer page, Integer limit, String sort) {
        Page<Movie> movies = movieService.getMovies(page, limit, sort);
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
    public ResponseEntity<YearListResponse> getYears() {
        List<Integer> years = movieService.getYears();
        YearListResponse yearListResponse = MovieMapper.mapToYearListResponse(years);

        return ResponseEntity.ok(yearListResponse);
    }

    @Override
    public ResponseEntity<MovieDetailsResponse> getMovie(UUID movieId) {
        Movie movie = movieService.getMovieWithDetails(movieId);
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
    public ResponseEntity<Void> deleteMovie(UUID movieId) {
        movieService.deleteMovie(movieId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> addRating(UUID movieId, MovieUserRating movieUserRating) {
        movieService.addRating(movieId, movieUserRating.getRating());

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MovieUserRating> getUserRating(UUID movieId) {
        Double rating = movieService.getRating(movieId);
        MovieUserRating movieUserRating = MovieMapper.mapToMovieUserRating(rating);

        return ResponseEntity.ok(movieUserRating);
    }

    @Override
    public ResponseEntity<Void> addComment(UUID movieId, AddCommentRequest addCommentRequest) {
        movieService.addComment(movieId, addCommentRequest.getText());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<CommentInfoPage> getComments(UUID movieId, Integer page, Integer limit, String sort) {
        Page<Comment> comments = movieService.getComments(movieId, page, limit, sort);
        CommentInfoPage commentInfoPage = CommentInfoMapper.mapToCommentInfoPage(comments);

        return ResponseEntity.ok(commentInfoPage);
    }

}
