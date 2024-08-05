package com.movie_manager.mapper.dto;

import com.movie_manager.dto.MovieCastDetailsResponse;
import com.movie_manager.dto.MovieCastRequest;
import com.movie_manager.dto.MovieCastResponse;
import com.movie_manager.entity.Actor;
import com.movie_manager.entity.MovieCast;

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
                                                             .actorId(cast.getActor().getActorId())
                                                             .name(cast.getActor().getName())
                                                             .role(cast.getRole())
                                                             .build())
                        .toList();
    }

    public static List<MovieCastResponse> mapToMovieCastResponse(List<MovieCast> movieCast) {
        return movieCast.stream()
                        .map(cast -> MovieCastResponse.builder()
                                                      .name(cast.getActor().getName())
                                                      .role(cast.getRole())
                                                      .build())
                        .toList();
    }

}
