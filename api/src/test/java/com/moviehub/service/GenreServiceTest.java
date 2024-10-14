package com.moviehub.service;

import com.moviehub.entity.Genre;
import com.moviehub.entity.Genre_;
import com.moviehub.exception.GenreAlreadyExistsException;
import com.moviehub.exception.GenreNotFoundException;
import com.moviehub.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private static final UUID GENRE_ID = UUID.fromString("d6a36f8c-9bfa-4d76-8f54-cd895acb9f07");
    private static final String GENRE_ACTION = "Action";
    private static final String GENRE_COMEDY = "Comedy";
    private static final String GENRE_THRILLER = "Thriller";

    @Test
    void shouldGetSavedGenres() {
        Genre genre = createGenre(GENRE_ID, GENRE_ACTION);
        List<Genre> genres = List.of(genre);

        when(genreRepository.findByName(GENRE_ACTION)).thenReturn(Optional.of(genre));

        List<Genre> savedGenres = genreService.getSavedGenres(genres);

        assertThat(savedGenres).hasSize(1);
        assertThat(savedGenres.getFirst()
                              .getName()).isEqualTo(GENRE_ACTION);
    }

    @Test
    void shouldThrowGenreNotFoundExceptionWhenGettingSavedGenre() {
        List<Genre> genres = List.of(createGenre(null, GENRE_ACTION));

        when(genreRepository.findByName(GENRE_ACTION)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GenreNotFoundException.class)
            .isThrownBy(() -> genreService.getSavedGenres(genres));
    }

    @Test
    void shouldGetAllGenres() {
        Genre genre1 = createGenre(GENRE_ID, GENRE_ACTION);
        Genre genre2 = createGenre(UUID.randomUUID(), GENRE_COMEDY);

        List<Genre> genres = List.of(genre1, genre2);

        when(genreRepository.findAll(Sort.by(Genre_.NAME)
                                         .ascending())).thenReturn(genres);

        List<Genre> retrievedGenres = genreService.getGenres();

        assertThat(retrievedGenres).hasSize(2);
        assertThat(retrievedGenres.stream()
                                  .map(Genre::getName)
                                  .toList())
            .containsExactlyInAnyOrder(GENRE_ACTION, GENRE_COMEDY);
    }

    @Test
    void shouldGetGenreById() {
        Genre genre = createGenre(GENRE_ID, GENRE_ACTION);

        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));

        Genre retrievedGenre = genreService.getGenre(GENRE_ID);

        assertThat(retrievedGenre.getName()).isEqualTo(GENRE_ACTION);
    }

    @Test
    void shouldThrowGenreNotFoundExceptionWhenGettingGenreById() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GenreNotFoundException.class)
            .isThrownBy(() -> genreService.getGenre(GENRE_ID));
    }

    @Test
    void shouldAddGenre() {
        when(genreRepository.existsByName(GENRE_COMEDY)).thenReturn(false);
        when(genreRepository.save(any(Genre.class))).thenReturn(createGenre(UUID.randomUUID(), GENRE_COMEDY));

        Genre addedGenre = genreService.addGenre(GENRE_COMEDY);

        assertThat(addedGenre.getName()).isEqualTo(GENRE_COMEDY);
    }

    @Test
    void shouldThrowGenreAlreadyExistsExceptionWhenAddingDuplicateGenre() {
        when(genreRepository.existsByName(GENRE_COMEDY)).thenReturn(true);

        assertThatExceptionOfType(GenreAlreadyExistsException.class)
            .isThrownBy(() -> genreService.addGenre(GENRE_COMEDY));
    }

    @Test
    void shouldUpdateGenre() {
        Genre genre = createGenre(GENRE_ID, GENRE_ACTION);

        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);

        Genre updatedGenre = genreService.updateGenre(GENRE_ID, GENRE_THRILLER);

        assertThat(updatedGenre.getName()).isEqualTo(GENRE_THRILLER);
    }

    @Test
    void shouldThrowGenreNotFoundExceptionWhenUpdatingNonExistentGenre() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(GenreNotFoundException.class)
            .isThrownBy(() -> genreService.updateGenre(GENRE_ID, GENRE_THRILLER));
    }

    private static Genre createGenre(UUID id, String name) {
        return Genre.builder()
                    .id(id)
                    .name(name)
                    .build();
    }

}