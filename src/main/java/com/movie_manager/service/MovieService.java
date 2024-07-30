package com.movie_manager.service;

import com.movie_manager.dto.AddMovieRequest;
import com.movie_manager.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public void addMovie(AddMovieRequest addMovieRequest) {
        // TODO: pouzit modelMapper ? (funguje i s arrays ?)
    }

}
