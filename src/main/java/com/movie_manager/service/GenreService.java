package com.movie_manager.service;

import com.movie_manager.entity.Genre;
import com.movie_manager.exception.GenreNotFoundException;
import com.movie_manager.repository.GenreRepository;
import com.movie_manager.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public List<Genre> getSavedGenres(List<Genre> genres) {
        List<Genre> existingGenres = new ArrayList<>();

        for (Genre genre : genres) {
            Genre existingGenre = genreRepository.findByName(genre.getName())
                                                 .orElseGet(() -> genreRepository.save(genre));
            existingGenres.add(existingGenre);
        }

        return existingGenres;
    }

    @Transactional
    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    @Transactional
    public Genre getGenre(String genreId) {
        return genreRepository.findById(genreId)
                              .orElseThrow(() -> new GenreNotFoundException("Genre with ID: " + genreId + " not found"));
    }

}
