package com.moviehub.mapper.dto;

import com.moviehub.dto.AddDirectorRequest;
import com.moviehub.dto.DirectorDetailsResponse;
import com.moviehub.dto.DirectorPage;
import com.moviehub.dto.DirectorResponse;
import com.moviehub.dto.GenderName;
import com.moviehub.dto.UpdateDirectorRequest;
import com.moviehub.entity.Director;
import com.moviehub.entity.Gender;
import org.springframework.data.domain.Page;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between Director entities and Data Transfer Objects (DTOs).
public class DirectorMapper {

    // Private constructor to prevent instantiation.
    private DirectorMapper() {
    }

    /// Maps a director's name to a Director entity.
    ///
    /// @param name The name of the director.
    /// @return A Director entity or null if the name is null.
    public static Director mapToDirector(String name) {
        if (name == null) {
            return null;
        }

        return Director.builder()
                       .name(name)
                       .build();
    }

    /// Maps a Director entity to a DirectorResponse DTO.
    ///
    /// @param director The Director entity to map.
    /// @return A DirectorResponse DTO containing the director's information.
    public static DirectorResponse mapToDirectorResponse(Director director) {
        return DirectorResponse.builder()
                               .id(director.getId())
                               .name(director.getName())
                               .build();
    }

    /// Maps an AddDirectorRequest DTO to a Director entity.
    ///
    /// @param addDirectorRequest The DTO containing director information to map.
    /// @return A Director entity populated with data from the DTO.
    public static Director mapToDirector(AddDirectorRequest addDirectorRequest) {
        return Director.builder()
                       .name(addDirectorRequest.getName())
                       .bio(addDirectorRequest.getBio())
                       .gender(Gender.valueOf(addDirectorRequest.getGender().name()))
                       .build();
    }

    /// Maps an UpdateDirectorRequest DTO to a Director entity.
    ///
    /// @param updateDirectorRequest The DTO containing updated director information to map.
    /// @return A Director entity populated with updated data from the DTO.
    public static Director mapToDirector(UpdateDirectorRequest updateDirectorRequest) {
        return Director.builder()
                       .name(updateDirectorRequest.getName())
                       .bio(updateDirectorRequest.getBio())
                       .gender(Gender.valueOf(updateDirectorRequest.getGender().name()))
                       .build();
    }

    /// Maps a Director entity to a DirectorDetailsResponse DTO.
    ///
    /// @param director The Director entity to map.
    /// @return A DirectorDetailsResponse DTO containing detailed director information.
    public static DirectorDetailsResponse mapToDirectorDetailsResponse(Director director) {
        return DirectorDetailsResponse.builder()
                                      .id(director.getId())
                                      .name(director.getName())
                                      .bio(director.getBio())
                                      .gender(GenderName.fromValue(director.getGender().getValue()))
                                      .build();
    }

    /// Maps a Page of Director entities to a DirectorPage DTO.
    ///
    /// @param directors The Page of Director entities to map.
    /// @return A DirectorPage DTO containing a paginated list of directors.
    public static DirectorPage mapToDirectorPage(Page<Director> directors) {
        return DirectorPage.builder()
                           .content(mapToDirectorDetailsResponseList(directors))
                           .pageable(PageableMapper.mapToPageableDTO(directors.getPageable()))
                           .last(directors.isLast())
                           .totalElements(directors.getTotalElements())
                           .totalPages(directors.getTotalPages())
                           .first(directors.isFirst())
                           .size(directors.getSize())
                           .number(directors.getNumber())
                           .sort(SortMapper.mapToSortDTO(directors.getSort()))
                           .numberOfElements(directors.getNumberOfElements())
                           .empty(directors.isEmpty())
                           .build();
    }

    /// Maps a Page of Director entities to a list of DirectorDetailsResponse DTOs.
    ///
    /// @param directors The Page of Director entities to map.
    /// @return A list of DirectorDetailsResponse DTOs.
    private static List<DirectorDetailsResponse> mapToDirectorDetailsResponseList(Page<Director> directors) {
        return directors.stream()
                        .map(DirectorMapper::mapToDirectorDetailsResponse)
                        .toList();
    }

}
