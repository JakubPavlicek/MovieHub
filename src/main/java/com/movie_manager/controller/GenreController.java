package com.movie_manager.controller;

import com.movie_manager.GenresApi;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController implements GenresApi {

    private final GenreService genreService;
    private final MovieService movieService;
    private final GenreMapper genreMapper;
    private final MovieMapper movieMapper;

    @Override
    public ResponseEntity<GenreResponse> getGenres() {
        List<Genre> genres = genreService.getGenres();
        GenreResponse genreResponse = genreMapper.mapToGenreResponse(genres);

        return ResponseEntity.ok(genreResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithGenre(String genreId, Integer page, Integer limit, String sort) {
        Genre genre = genreService.getGenre(genreId);
        Page<Movie> movies = movieService.getMoviesByGenre(genre, page, limit, sort);
        MoviePage moviePage = movieMapper.map(movies);

        return ResponseEntity.ok(moviePage);
    }

}
