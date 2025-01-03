package com.moviehub.controller;

import com.moviehub.DirectorsApi;
import com.moviehub.dto.AddDirectorRequest;
import com.moviehub.dto.DirectorDetailsResponse;
import com.moviehub.dto.DirectorPage;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.UpdateDirectorRequest;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.mapper.dto.DirectorMapper;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.service.DirectorService;
import com.moviehub.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Controller class for managing directors.
/// It provides endpoints for adding directors, retrieving director details,
/// updating directors, and retrieving movies associated with a specific director.
@RestController
@RequiredArgsConstructor
public class DirectorController implements DirectorsApi {

    /// Service for handling operations related to directors.
    private final DirectorService directorService;
    /// Service for handling operations related to movies.
    private final MovieService movieService;

    @Override
    public ResponseEntity<DirectorDetailsResponse> addDirector(AddDirectorRequest addDirectorRequest) {
        Director director = DirectorMapper.mapToDirector(addDirectorRequest);
        director = directorService.addDirector(director);
        DirectorDetailsResponse directorReponse = DirectorMapper.mapToDirectorDetailsResponse(director);

        return ResponseEntity.status(HttpStatus.CREATED).body(directorReponse);
    }

    @Override
    public ResponseEntity<DirectorPage> getDirectors(Integer page, Integer limit, String name) {
        Page<Director> directors = directorService.getDirectors(page, limit, name);
        DirectorPage directorPage = DirectorMapper.mapToDirectorPage(directors);

        return ResponseEntity.ok(directorPage);
    }

    @Override
    public ResponseEntity<DirectorDetailsResponse> getDirector(UUID directorId) {
        Director director = directorService.getDirector(directorId);
        DirectorDetailsResponse directorDetailsResponse = DirectorMapper.mapToDirectorDetailsResponse(director);

        return ResponseEntity.ok(directorDetailsResponse);
    }

    @Override
    public ResponseEntity<DirectorDetailsResponse> updateDirector(UUID directorId, UpdateDirectorRequest updateDirectorRequest) {
        Director director = DirectorMapper.mapToDirector(updateDirectorRequest);
        director = directorService.updateDirector(directorId, director);
        DirectorDetailsResponse directorResponse = DirectorMapper.mapToDirectorDetailsResponse(director);

        return ResponseEntity.ok(directorResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithDirector(UUID directorId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithDirector(directorId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
