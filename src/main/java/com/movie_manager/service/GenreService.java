package com.movie_manager.service;

import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.repository.GenreRepository;
import com.movie_manager.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    private final ParseService parseService;

    @Transactional
    public List<Genre> getExistingGenres(List<Genre> genres) {
        List<Genre> existingGenres = new ArrayList<>();

        genres.forEach(genre -> {
            Genre existingGenre = genreRepository.findByName(genre.getName())
                                                 .orElseGet(() -> genreRepository.save(genre));
            existingGenres.add(existingGenre);
        });

        return existingGenres;
    }

    @Transactional
    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenre(String genreId) {
        return genreRepository.findById(genreId)
                              .orElseThrow(() -> new RuntimeException("Genre with ID: " + genreId + " not found"));
    }

    public Page<Movie> getMoviesWithGenre(String genreId, Integer page, Integer limit, String sort) {
        Genre genre = getGenre(genreId);

        Pageable pageable = PageRequest.of(page, limit, parseService.parseSort(sort));

        return movieRepository.findAllByGenresContaining(genre, pageable);
    }

}
