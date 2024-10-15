package com.moviehub.mapper.dto;

import com.moviehub.dto.MovieCastDetailsResponse;
import com.moviehub.dto.MovieCastRequest;
import com.moviehub.entity.Actor;
import com.moviehub.entity.MovieCast;

import java.util.List;

public class MovieCastMapper {

    private MovieCastMapper() {
    }

    public static List<MovieCast> mapToMovieCasts(List<MovieCastRequest> castRequests) {
        return castRequests.stream()
                           .map(MovieCastMapper::mapToMovieCast)
                           .toList();
    }

    private static MovieCast mapToMovieCast(MovieCastRequest castRequest) {
        Actor actor = Actor.builder()
                           .name(castRequest.getName())
                           .build();

        return MovieCast.builder()
                        .actor(actor)
                        .role(castRequest.getRole())
                        .build();
    }

    public static List<MovieCastDetailsResponse> mapToMovieCastDetailsResponse(List<MovieCast> movieCast) {
        return movieCast.stream()
                        .map(MovieCastMapper::mapToMovieCastDetailsResponse)
                        .toList();
    }

    private static MovieCastDetailsResponse mapToMovieCastDetailsResponse(MovieCast movieCast) {
        return MovieCastDetailsResponse.builder()
                                       .id(movieCast.getActor().getId())
                                       .name(movieCast.getActor().getName())
                                       .role(movieCast.getRole())
                                       .build();
    }

}
