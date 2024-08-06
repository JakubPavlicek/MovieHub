package com.movie_manager.controller;

import com.movie_manager.GenresApi;
import com.movie_manager.dto.AddGenreRequest;
import com.movie_manager.dto.GenreDTO;
import com.movie_manager.dto.GenreResponse;
import com.movie_manager.dto.MoviePage;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.mapper.dto.GenreMapper;
import com.movie_manager.mapper.dto.MovieMapper;
import com.movie_manager.service.GenreService;
import com.movie_manager.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController implements GenresApi {

    private final GenreService genreService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<GenreDTO> addGenre(AddGenreRequest addGenreRequest) {
        Genre genre = genreService.addGenre(addGenreRequest.getName());
        GenreDTO genreDTO = GenreMapper.mapToGenreDTO(genre);

        return ResponseEntity.status(HttpStatus.CREATED).body(genreDTO);
    }

    @Override
    public ResponseEntity<GenreResponse> getGenres() {
        List<Genre> genres = genreService.getGenres();
        GenreResponse genreResponse = GenreMapper.mapToGenreResponse(genres);

        return ResponseEntity.ok(genreResponse);
    }

    @Override
    public ResponseEntity<GenreDTO> getGenreById(String genreId) {
        Genre genre = genreService.getGenre(genreId);
        GenreDTO genreDTO = GenreMapper.mapToGenreDTO(genre);

        return ResponseEntity.ok(genreDTO);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithGenre(String genreId, Integer page, Integer limit, String sort) {
        Page<Movie> movies = movieService.getMoviesByGenre(genreId, page, limit, sort);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
