package com.movie_manager.controller;

import com.movie_manager.DirectorsApi;
import com.movie_manager.dto.AddDirectorRequest;
import com.movie_manager.dto.DirectorDetailsResponse;
import com.movie_manager.dto.DirectorPage;
import com.movie_manager.dto.MoviePage;
import com.movie_manager.dto.UpdateDirectorRequest;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Movie;
import com.movie_manager.mapper.dto.DirectorMapper;
import com.movie_manager.mapper.dto.MovieMapper;
import com.movie_manager.service.DirectorService;
import com.movie_manager.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DirectorController implements DirectorsApi {

    private final DirectorService directorService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<DirectorDetailsResponse> addDirector(AddDirectorRequest addDirectorRequest) {
        Director director = DirectorMapper.mapToDirector(addDirectorRequest);
        director = directorService.addDirector(director);
        DirectorDetailsResponse directorReponse = DirectorMapper.mapToDirectorDetailsResponse(director);

        return ResponseEntity.status(HttpStatus.CREATED).body(directorReponse);
    }

    @Override
    public ResponseEntity<DirectorDetailsResponse> getDirectorById(String directorId) {
        Director director = directorService.getDirector(directorId);
        DirectorDetailsResponse directorDetailsResponse = DirectorMapper.mapToDirectorDetailsResponse(director);

        return ResponseEntity.ok(directorDetailsResponse);
    }

    @Override
    public ResponseEntity<DirectorPage> getDirectors(Integer page, Integer limit) {
        Page<Director> directors = directorService.getDirectors(page, limit);
        DirectorPage directorPage = DirectorMapper.mapToDirectorPage(directors);

        return ResponseEntity.ok(directorPage);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithDirector(String directorId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithDirector(directorId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

    @Override
    public ResponseEntity<DirectorDetailsResponse> updateDirector(String directorId, UpdateDirectorRequest updateDirectorRequest) {
        Director director = DirectorMapper.mapToDirector(updateDirectorRequest);
        director = directorService.updateDirector(directorId, director);
        DirectorDetailsResponse directorResponse = DirectorMapper.mapToDirectorDetailsResponse(director);

        return ResponseEntity.ok(directorResponse);
    }

}
