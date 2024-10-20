package com.moviehub.mapper.dto;

import com.moviehub.dto.GenreDetailsResponse;
import com.moviehub.dto.GenreListResponse;
import com.moviehub.entity.Genre;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between Genre entities and Data Transfer Objects (DTOs).
public class GenreMapper {

    // Private constructor to prevent instantiation.
    private GenreMapper() {
    }

    /// Maps a list of genre names to a list of Genre entities.
    ///
    /// @param genres A list of genre names to map.
    /// @return A list of Genre entities corresponding to the provided names.
    public static List<Genre> mapToGenres(List<String> genres) {
        return genres.stream()
                     .map(genreName -> Genre.builder()
                                            .name(genreName)
                                            .build())
                     .toList();
    }

    /// Maps a list of Genre entities to a list of GenreDetailsResponse DTOs.
    ///
    /// @param genres A list of Genre entities to map.
    /// @return A list of GenreDetailsResponse DTOs containing detailed genre information.
    public static List<GenreDetailsResponse> mapToGenreDetailsResponseList(List<Genre> genres) {
        return genres.stream()
                     .map(GenreMapper::mapToGenreDetailsResponse)
                     .toList();
    }

    /// Maps a list of Genre entities to a list of genre names.
    ///
    /// @param genres A list of Genre entities to map.
    /// @return A list of genre names extracted from the provided entities.
    public static List<String> mapToGenreNames(List<Genre> genres) {
        return genres.stream()
                     .map(Genre::getName)
                     .toList();
    }

    /// Maps a Genre entity to a GenreDetailsResponse DTO.
    ///
    /// @param genre The Genre entity to map.
    /// @return A GenreDetailsResponse DTO containing the genre's information.
    public static GenreDetailsResponse mapToGenreDetailsResponse(Genre genre) {
        return GenreDetailsResponse.builder()
                                   .id(genre.getId())
                                   .name(genre.getName())
                                   .build();
    }

    /// Maps a list of Genre entities to a GenreListResponse DTO.
    ///
    /// @param genres A list of Genre entities to map.
    /// @return A GenreListResponse DTO containing a list of genres.
    public static GenreListResponse mapToGenreListResponse(List<Genre> genres) {
        return GenreListResponse.builder()
                                .genres(mapToGenreDetailsResponseList(genres))
                                .build();
    }

}
