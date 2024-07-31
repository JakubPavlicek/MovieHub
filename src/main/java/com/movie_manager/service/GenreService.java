package com.movie_manager.service;

import com.movie_manager.entity.Genre;
import com.movie_manager.repository.GenreRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

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

}
