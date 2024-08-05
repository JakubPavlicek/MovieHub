package com.movie_manager.mapper.dto;

import com.movie_manager.dto.GenreDTO;
import com.movie_manager.dto.GenreResponse;
import com.movie_manager.entity.Genre;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreMapper {

    private GenreMapper() {
    }

    public static List<Genre> mapToGenres(List<String> genres) {
        return genres.stream()
                     .map(genreName -> Genre.builder().name(genreName).build())
                     .toList();
    }

    public static List<GenreDTO> mapToGenreDTOS(List<Genre> genres) {
        return genres.stream()
                     .map(GenreMapper::mapToGenreDTO)
                     .toList();
    }

    public static GenreResponse mapToGenreResponse(List<Genre> genres) {
        List<GenreDTO> genreDTOS = genres.stream()
                                         .map(GenreMapper::mapToGenreDTO)
                                         .toList();

        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setGenres(genreDTOS);

        return genreResponse;
    }

    public static List<String> mapToGenreNames(List<Genre> genres) {
        return genres.stream()
                     .map(Genre::getName)
                     .toList();
    }

    private static GenreDTO mapToGenreDTO(Genre genre) {
        return GenreDTO.builder()
                       .genreId(genre.getGenreId())
                       .name(genre.getName())
                       .build();
    }

}
