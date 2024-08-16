package com.movie_manager.mapper.dto;

import com.movie_manager.dto.GenreDetailsResponse;
import com.movie_manager.dto.GenrePage;
import com.movie_manager.entity.Genre;
import org.springframework.data.domain.Page;
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

    public static GenrePage mapToGenrePage(Page<Genre> genres) {
        return GenrePage.builder()
                        .content(mapToGenreDetailsResponseList(genres))
                        .pageable(PageableMapper.mapToPageableDTO(genres.getPageable()))
                        .last(genres.isLast())
                        .totalElements(genres.getTotalElements())
                        .totalPages(genres.getTotalPages())
                        .first(genres.isFirst())
                        .size(genres.getSize())
                        .number(genres.getNumber())
                        .sort(SortMapper.mapToSortDTO(genres.getSort()))
                        .numberOfElements(genres.getNumberOfElements())
                        .empty(genres.isEmpty())
                        .build();
    }

    private static List<GenreDetailsResponse> mapToGenreDetailsResponseList(Page<Genre> genres) {
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
                                   .genreId(genre.getGenreId())
                                   .name(genre.getName())
                                   .build();
    }

}
