package com.movie_manager.mapper;

import com.movie_manager.dto.AddMovieRequest;
import com.movie_manager.dto.MovieResponse;
import com.movie_manager.dto.UpdateMovieRequest;
import com.movie_manager.entity.Movie;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieMapper {

    private final ModelMapper modelMapper;

    public Movie map(AddMovieRequest addMovieRequest) {
        return modelMapper.map(addMovieRequest, Movie.class);
    }

    public Movie map(UpdateMovieRequest updateMovieRequest) {
        return modelMapper.map(updateMovieRequest, Movie.class);
    }

    public MovieResponse map(Movie movie) {
        return modelMapper.map(movie, MovieResponse.class);
    }
}
