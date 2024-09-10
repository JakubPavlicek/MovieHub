package com.moviehub.mapper.dto;

import com.moviehub.dto.GenreDetailsResponse;
import com.moviehub.dto.GenreListResponse;
import com.moviehub.entity.Genre;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreMapper {

    private GenreMapper() {
    }

    public static List<Genre> mapToGenres(List<String> genres) {
        return genres.stream()
                     .map(genreName -> Genre.builder()
                                            .name(genreName)
                                            .build())
                     .toList();
    }

    public static List<GenreDetailsResponse> mapToGenreDetailsResponseList(List<Genre> genres) {
        return genres.stream()
                     .map(GenreMapper::mapToGenreDetailsResponse)
                     .toList();
    }

    public static List<String> mapToGenreNames(List<Genre> genres) {
        return genres.stream()
                     .map(Genre::getName)
                     .toList();
    }

    public static GenreDetailsResponse mapToGenreDetailsResponse(Genre genre) {
        return GenreDetailsResponse.builder()
                                   .id(genre.getId())
                                   .name(genre.getName())
                                   .build();
    }

    public static GenreListResponse mapToGenreListResponse(List<Genre> genres) {
        return GenreListResponse.builder()
                                .genres(mapToGenreDetailsResponseList(genres))
                                .build();
    }

}
