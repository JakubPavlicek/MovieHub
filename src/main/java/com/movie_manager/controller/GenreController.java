package com.movie_manager.controller;

import com.movie_manager.GenresApi;
import com.movie_manager.dto.AddGenreRequest;
import com.movie_manager.dto.GenreDetailsResponse;
import com.movie_manager.dto.GenrePage;
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

@RestController
@RequiredArgsConstructor
public class GenreController implements GenresApi {

    private final GenreService genreService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<GenreDetailsResponse> addGenre(AddGenreRequest addGenreRequest) {
        Genre genre = genreService.addGenre(addGenreRequest.getName());
        GenreDetailsResponse genreReponse = GenreMapper.mapToGenreDetailsResponse(genre);

        return ResponseEntity.status(HttpStatus.CREATED).body(genreReponse);
    }

    @Override
    public ResponseEntity<GenrePage> getGenres(Integer page, Integer limit) {
        Page<Genre> genres = genreService.getGenres(page, limit);
        GenrePage genrePage = GenreMapper.mapToGenrePage(genres);

        return ResponseEntity.ok(genrePage);
    }

    @Override
    public ResponseEntity<GenreDetailsResponse> getGenreById(String genreId) {
        Genre genre = genreService.getGenre(genreId);
        GenreDetailsResponse genreReponse = GenreMapper.mapToGenreDetailsResponse(genre);

        return ResponseEntity.ok(genreReponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithGenre(String genreId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesByGenre(genreId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
