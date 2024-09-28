package com.moviehub.service;

import com.moviehub.entity.Genre;
import com.moviehub.entity.Genre_;
import com.moviehub.exception.GenreAlreadyExistsException;
import com.moviehub.exception.GenreNotFoundException;
import com.moviehub.repository.GenreRepository;
import com.moviehub.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    public List<Genre> getSavedGenres(List<Genre> genres) {
        return genres.stream()
                     .map(this::getSavedGenre)
                     .collect(Collectors.toCollection(ArrayList::new));
    }

    private Genre getSavedGenre(Genre genre) {
        return genreRepository.findByName(genre.getName())
                              .orElseThrow(() -> new GenreNotFoundException("Genre with name: " + genre.getName() + " not found"));
    }

    public List<Genre> getGenres() {
        Sort sort = Sort.by(Sort.Direction.ASC, Genre_.NAME);

        return genreRepository.findAll(sort);
    }

    public Genre getGenre(String genreId) {
        return genreRepository.findById(genreId)
                              .orElseThrow(() -> new GenreNotFoundException("Genre with ID: " + genreId + " not found"));
    }

    public Genre addGenre(String name) {
        if (genreRepository.existsByName(name)) {
            throw new GenreAlreadyExistsException("Genre with name: " + name + " already exists");
        }

        Genre genre = new Genre();
        genre.setName(name);

        return genreRepository.save(genre);
    }

    public Genre updateGenre(String genreId, String name) {
        Genre genre = getGenre(genreId);
        genre.setName(name);

        return genreRepository.save(genre);
    }

}
