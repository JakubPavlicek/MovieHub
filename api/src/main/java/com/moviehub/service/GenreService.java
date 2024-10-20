package com.moviehub.service;

import com.moviehub.entity.Genre;
import com.moviehub.entity.Genre_;
import com.moviehub.exception.GenreAlreadyExistsException;
import com.moviehub.exception.GenreNotFoundException;
import com.moviehub.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getSavedGenres(List<Genre> genres) {
        log.info("retrieving saved genres");

        return genres.stream()
                     .map(this::getSavedGenre)
                     .collect(Collectors.toCollection(ArrayList::new));
    }

    private Genre getSavedGenre(Genre genre) {
        return genreRepository.findByName(genre.getName())
                              .orElseThrow(() -> new GenreNotFoundException("Genre with name: " + genre.getName() + " not found"));
    }

    public List<Genre> getGenres() {
        log.info("retrieving all genres");

        Sort sort = Sort.by(Genre_.NAME).ascending();

        return genreRepository.findAll(sort);
    }

    public Genre getGenre(UUID genreId) {
        log.info("retrieving genre: {}", genreId);

        return genreRepository.findById(genreId)
                              .orElseThrow(() -> new GenreNotFoundException("Genre with ID: " + genreId + " not found"));
    }

    public Genre addGenre(String name) {
        if (genreRepository.existsByName(name)) {
            throw new GenreAlreadyExistsException("Genre with name: " + name + " already exists");
        }

        log.info("adding new genre: {}", name);

        Genre genre = new Genre();
        genre.setName(name);

        return genreRepository.save(genre);
    }

    public Genre updateGenre(UUID genreId, String name) {
        log.info("updating genre: {} to name: {}", genreId, name);

        Genre genre = getGenre(genreId);
        genre.setName(name);

        return genreRepository.save(genre);
    }

}
