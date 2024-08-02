package com.movie_manager.mapper.dto;

import com.movie_manager.dto.GenreDTO;
import com.movie_manager.dto.GenreResponse;
import com.movie_manager.entity.Genre;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreMapper {

    private final ModelMapper modelMapper;

    public GenreResponse map(List<Genre> genres) {
        List<GenreDTO> genreDTOS = genres.stream()
                                         .map(genre -> modelMapper.map(genre, GenreDTO.class))
                                         .toList();

        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setGenres(genreDTOS);

        return genreResponse;
    }

}
