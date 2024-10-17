package com.moviehub.controller;

import com.moviehub.GenresApi;
import com.moviehub.dto.AddGenreRequest;
import com.moviehub.dto.GenreDetailsResponse;
import com.moviehub.dto.GenreListResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.mapper.dto.GenreMapper;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.service.GenreService;
import com.moviehub.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<GenreDetailsResponse> getGenre(UUID genreId) {
        Genre genre = genreService.getGenre(genreId);
        GenreDetailsResponse genreReponse = GenreMapper.mapToGenreDetailsResponse(genre);

        return ResponseEntity.ok(genreReponse);
    }

    @Override
    public ResponseEntity<GenreListResponse> getGenres() {
        List<Genre> genres = genreService.getGenres();
        GenreListResponse genreListResponse = GenreMapper.mapToGenreListResponse(genres);

        return ResponseEntity.ok(genreListResponse);
    }

    @Override
    public ResponseEntity<GenreDetailsResponse> updateGenre(UUID genreId, AddGenreRequest addGenreRequest) {
        Genre genre = genreService.updateGenre(genreId, addGenreRequest.getName());
        GenreDetailsResponse genreDetailsResponse = GenreMapper.mapToGenreDetailsResponse(genre);

        return ResponseEntity.ok(genreDetailsResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithGenre(UUID genreId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithGenre(genreId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
