package com.moviehub.mapper.dto;

import com.moviehub.dto.AddDirectorRequest;
import com.moviehub.dto.DirectorDTO;
import com.moviehub.dto.DirectorDetailsResponse;
import com.moviehub.dto.DirectorPage;
import com.moviehub.dto.GenderName;
import com.moviehub.dto.UpdateDirectorRequest;
import com.moviehub.entity.Director;
import org.springframework.data.domain.Page;

import java.util.List;

public class DirectorMapper {

    private DirectorMapper() {
    }

    public static Director mapToDirector(String name) {
        if (name == null) {
            return null;
        }

        return Director.builder()
                       .name(name)
                       .build();
    }

    public static DirectorDTO mapToDirectorDTO(Director director) {
        return DirectorDTO.builder()
                          .id(director.getId())
                          .name(director.getName())
                          .build();
    }

    public static Director mapToDirector(AddDirectorRequest addDirectorRequest) {
        return Director.builder()
                       .name(addDirectorRequest.getName())
                       .bio(addDirectorRequest.getBio())
                       .gender(GenderMapper.mapToGender(addDirectorRequest.getGender()))
                       .build();
    }

    public static Director mapToDirector(UpdateDirectorRequest updateDirectorRequest) {
        return Director.builder()
                       .name(updateDirectorRequest.getName())
                       .bio(updateDirectorRequest.getBio())
                       .gender(GenderMapper.mapToGender(updateDirectorRequest.getGender()))
                       .build();
    }

    public static DirectorDetailsResponse mapToDirectorDetailsResponse(Director director) {
        return DirectorDetailsResponse.builder()
                                      .id(director.getId())
                                      .name(director.getName())
                                      .bio(director.getBio())
                                      .gender(GenderName.fromValue(director.getGender().getName()))
                                      .build();
    }

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

    private static List<DirectorDetailsResponse> mapToDirectorDetailsResponseList(Page<Director> directors) {
        return directors.stream()
                        .map(DirectorMapper::mapToDirectorDetailsResponse)
                        .toList();
    }

}
