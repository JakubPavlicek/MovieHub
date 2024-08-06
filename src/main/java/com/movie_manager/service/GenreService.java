package com.movie_manager.service;

import com.movie_manager.entity.Genre;
import com.movie_manager.exception.GenreAlreadyExistsException;
import com.movie_manager.exception.GenreNotFoundException;
import com.movie_manager.repository.GenreRepository;
import com.movie_manager.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public Set<Genre> getSavedGenres(Set<Genre> genres) {
        return genres.stream()
                     .map(this::getSavedGenre)
                     .collect(Collectors.toCollection(HashSet::new));
    }

    private Genre getSavedGenre(Genre genre) {
        return genreRepository.findByName(genre.getName())
                              .orElseGet(() -> genreRepository.save(genre));
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

    @Transactional
    public Genre addGenre(String name) {
        if (genreRepository.existsByName(name)) {
            throw new GenreAlreadyExistsException("Genre with name: " + name + " already exists");
        }

        Genre genre = new Genre();
        genre.setName(name);

        return genreRepository.save(genre);
    }

}
