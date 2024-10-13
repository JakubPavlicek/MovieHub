package com.moviehub.mapper.dto;

import com.moviehub.dto.GenreDetailsResponse;
import com.moviehub.dto.GenreListResponse;
import com.moviehub.entity.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GenreMapperTest {
    
    private static final String ACTION = "Action";
    private static final String DRAMA = "Drama";
    private static final String COMEDY = "Comedy";

    private static final UUID FIRST_GENRE_ID = UUID.fromString("c3418739-b313-4c36-996d-f7f66e5f175f");
    private static final UUID SECOND_GENRE_ID = UUID.fromString("14b60dc7-44a3-4aab-afeb-293d7dbee56e");

    @Test
    void shouldMapToGenres() {
        List<String> genreNames = createGenreNamesList();

        List<Genre> genres = GenreMapper.mapToGenres(genreNames);

        assertThat(genres).hasSize(3);
        assertThat(genres.get(0).getName()).isEqualTo(ACTION);
        assertThat(genres.get(1).getName()).isEqualTo(DRAMA);
        assertThat(genres.get(2).getName()).isEqualTo(COMEDY);
    }

    @Test
    void shouldMapToGenreDetailsResponseList() {
        List<Genre> genres = List.of(
            createGenre(FIRST_GENRE_ID, ACTION),
            createGenre(SECOND_GENRE_ID, DRAMA)
        );

        List<GenreDetailsResponse> responseList = GenreMapper.mapToGenreDetailsResponseList(genres);

        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getId()).isEqualTo(FIRST_GENRE_ID);
        assertThat(responseList.get(0).getName()).isEqualTo(ACTION);
        assertThat(responseList.get(1).getId()).isEqualTo(SECOND_GENRE_ID);
        assertThat(responseList.get(1).getName()).isEqualTo(DRAMA);
    }

    @Test
    void shouldMapToGenreNames() {
        List<Genre> genres = List.of(
            createGenre(FIRST_GENRE_ID, ACTION),
            createGenre(SECOND_GENRE_ID, COMEDY)
        );

        List<String> genreNames = GenreMapper.mapToGenreNames(genres);

        assertThat(genreNames).containsExactly(ACTION, COMEDY);
    }

    @Test
    void shouldMapToGenreDetailsResponse() {
        Genre genre = createGenre(FIRST_GENRE_ID, ACTION);

        GenreDetailsResponse response = GenreMapper.mapToGenreDetailsResponse(genre);

        assertThat(response.getId()).isEqualTo(FIRST_GENRE_ID);
        assertThat(response.getName()).isEqualTo(ACTION);
    }

    @Test
    void shouldMapToGenreListResponse() {
        List<Genre> genres = List.of(
            createGenre(FIRST_GENRE_ID, ACTION),
            createGenre(SECOND_GENRE_ID, COMEDY)
        );

        GenreListResponse genreListResponse = GenreMapper.mapToGenreListResponse(genres);

        assertThat(genreListResponse.getGenres()).hasSize(2);
        assertThat(genreListResponse.getGenres().get(0).getId()).isEqualTo(FIRST_GENRE_ID);
        assertThat(genreListResponse.getGenres().get(0).getName()).isEqualTo(ACTION);
        assertThat(genreListResponse.getGenres().get(1).getId()).isEqualTo(SECOND_GENRE_ID);
        assertThat(genreListResponse.getGenres().get(1).getName()).isEqualTo(COMEDY);
    }

    private Genre createGenre(UUID id, String name) {
        return Genre.builder()
                    .id(id)
                    .name(name)
                    .build();
    }

    private List<String> createGenreNamesList() {
        return List.of(ACTION, DRAMA, COMEDY);
    }

}