package com.movie_manager.mapper.dto;

import com.movie_manager.dto.AddDirectorRequest;
import com.movie_manager.dto.DirectorDTO;
import com.movie_manager.dto.DirectorDetailsResponse;
import com.movie_manager.dto.DirectorResponse;
import com.movie_manager.dto.GenderName;
import com.movie_manager.dto.UpdateDirectorRequest;
import com.movie_manager.entity.Director;

import java.util.List;

public class DirectorMapper {

    private DirectorMapper() {
    }

    public static Director mapToDirector(String name) {
        return Director.builder()
                       .name(name)
                       .build();
    }

    public static DirectorDTO mapToDirectorDTO(Director director) {
        return DirectorDTO.builder()
                          .directorId(director.getDirectorId())
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
                                      .directorId(director.getDirectorId())
                                      .name(director.getName())
                                      .bio(director.getBio())
                                      .gender(GenderName.fromValue(director.getGender().getName()))
                                      .build();
    }

    public static DirectorResponse mapToDirectorResponse(List<Director> directors) {
        List<DirectorDTO> directorDTOS = directors.stream()
                                                  .map(DirectorMapper::mapToDirectorDTO)
                                                  .toList();

        DirectorResponse directorResponse = new DirectorResponse();
        directorResponse.setDirectors(directorDTOS);

        return directorResponse;
    }

}
