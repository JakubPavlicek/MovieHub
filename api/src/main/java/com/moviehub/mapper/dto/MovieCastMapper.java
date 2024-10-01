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
                           .map(request -> {
                               Actor actor = Actor.builder().name(request.getName()).build();
                               return MovieCast.builder().actor(actor).role(request.getRole()).build();
                           })
                           .toList();
    }

    public static List<MovieCastDetailsResponse> mapToMovieCastDetailsResponse(List<MovieCast> movieCast) {
        return movieCast.stream()
                        .map(cast -> MovieCastDetailsResponse.builder()
                                                             .id(cast.getActor().getId())
                                                             .name(cast.getActor().getName())
                                                             .role(cast.getRole())
                                                             .build())
                        .toList();
    }

}
