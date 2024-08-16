package com.movie_manager.service;

import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Genre_;
import com.movie_manager.exception.GenreAlreadyExistsException;
import com.movie_manager.exception.GenreNotFoundException;
import com.movie_manager.repository.GenreRepository;
import com.movie_manager.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public List<Genre> getSavedGenres(List<Genre> genres) {
        return genres.stream()
                     .map(this::getSavedGenre)
                     .collect(Collectors.toCollection(ArrayList::new));
    }

    private Genre getSavedGenre(Genre genre) {
        return genreRepository.findByName(genre.getName())
                              .orElseThrow(() -> new GenreNotFoundException("Genre with name: " + genre.getName() + " not found"));
    }

    @Transactional
    public Page<Genre> getGenres(Integer page, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, Genre_.NAME);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return genreRepository.findAll(pageable);
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

    @Transactional
    public Genre updateGenre(String genreId, String name) {
        Genre genre = getGenre(genreId);
        genre.setName(name);

        return genreRepository.save(genre);
    }

}
