package com.movie_manager.mapper.dto;

import com.movie_manager.dto.DirectorDTO;
import com.movie_manager.entity.Director;

public class DirectorMapper {

    private DirectorMapper() {
    }

    public static Director mapToDirector(String name) {
        return Director.builder().name(name).build();
    }

    public static DirectorDTO mapToDirectorDTO(Director director) {
        return DirectorDTO.builder()
                          .directorId(director.getDirectorId())
                          .name(director.getName())
                          .build();
    }

}
