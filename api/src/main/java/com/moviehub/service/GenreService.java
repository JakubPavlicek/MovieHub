package com.moviehub.service;

import com.moviehub.entity.Genre;
import com.moviehub.entity.Genre_;
import com.moviehub.exception.GenreAlreadyExistsException;
import com.moviehub.exception.GenreNotFoundException;
import com.moviehub.repository.GenreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing genres.
/// This class provides methods for adding, retrieving, and updating genre entities.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class GenreService {

    /// Repository for managing Genre entities.
    private final GenreRepository genreRepository;

    /// Retrieves saved genres by checking each genre in the provided list.
    ///
    /// @param genres A list of Genre entities to check.
    /// @return A list of existing Genre entities found in the database.
    public List<Genre> getSavedGenres(List<Genre> genres) {
        log.info("retrieving saved genres");

        return genres.stream()
                     .map(this::getSavedGenre)
                     .collect(Collectors.toCollection(ArrayList::new));
    }

    /// Retrieves a saved genre by name.
    /// Throws a GenreNotFoundException if the genre does not exist.
    ///
    /// @param genre The Genre entity to check.
    /// @return The existing Genre entity if found.
    private Genre getSavedGenre(Genre genre) {
        return genreRepository.findByName(genre.getName())
                              .orElseThrow(() -> new GenreNotFoundException("Genre with name: " + genre.getName() + " not found"));
    }

    /// Retrieves all genres from the database, sorted by name in ascending order.
    ///
    /// @return A list of all Genre entities.
    public List<Genre> getGenres() {
        log.info("retrieving all genres");

        Sort sort = Sort.by(Genre_.NAME).ascending();

        return genreRepository.findAll(sort);
    }

    /// Retrieves a genre by its ID.
    /// Throws a GenreNotFoundException if the genre does not exist.
    ///
    /// @param genreId The ID of the genre to retrieve.
    /// @return The Genre entity associated with the specified ID.
    public Genre getGenre(UUID genreId) {
        log.info("retrieving genre: {}", genreId);

        return genreRepository.findById(genreId)
                              .orElseThrow(() -> new GenreNotFoundException("Genre with ID: " + genreId + " not found"));
    }

    /// Adds a new genre to the database.
    /// Throws a GenreAlreadyExistsException if a genre with the same name already exists.
    ///
    /// @param name The name of the genre to add.
    /// @return The newly created Genre entity.
    public Genre addGenre(String name) {
        if (genreRepository.existsByName(name)) {
            throw new GenreAlreadyExistsException("Genre with name: " + name + " already exists");
        }

        log.info("adding new genre: {}", name);

        Genre genre = new Genre();
        genre.setName(name);

        return genreRepository.save(genre);
    }

    /// Updates an existing genre's name.
    /// Throws a GenreNotFoundException if the genre does not exist.
    ///
    /// @param genreId The ID of the genre to update.
    /// @param name The new name for the genre.
    /// @return The updated Genre entity.
    public Genre updateGenre(UUID genreId, String name) {
        log.info("updating genre: {} to name: {}", genreId, name);

        Genre genre = getGenre(genreId);
        genre.setName(name);

        return genreRepository.save(genre);
    }

}
