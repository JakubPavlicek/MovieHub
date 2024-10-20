package com.moviehub.mapper.dto;

import com.moviehub.dto.MovieCastDetailsResponse;
import com.moviehub.dto.MovieCastRequest;
import com.moviehub.entity.Actor;
import com.moviehub.entity.MovieCast;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between MovieCast entities and Data Transfer Objects (DTOs).
public class MovieCastMapper {

    // Private constructor to prevent instantiation.
    private MovieCastMapper() {
    }

    /// Maps a list of MovieCastRequest DTOs to a list of MovieCast entities.
    ///
    /// @param castRequests A list of MovieCastRequest DTOs to map.
    /// @return A list of MovieCast entities corresponding to the provided requests.
    public static List<MovieCast> mapToMovieCasts(List<MovieCastRequest> castRequests) {
        return castRequests.stream()
                           .map(MovieCastMapper::mapToMovieCast)
                           .toList();
    }

    /// Maps a MovieCastRequest DTO to a MovieCast entity.
    ///
    /// @param castRequest The MovieCastRequest DTO to map.
    /// @return A MovieCast entity created from the provided request.
    private static MovieCast mapToMovieCast(MovieCastRequest castRequest) {
        Actor actor = Actor.builder()
                           .name(castRequest.getName())
                           .build();

        return MovieCast.builder()
                        .actor(actor)
                        .role(castRequest.getRole())
                        .build();
    }

    /// Maps a list of MovieCast entities to a list of MovieCastDetailsResponse DTOs.
    ///
    /// @param movieCast A list of MovieCast entities to map.
    /// @return A list of MovieCastDetailsResponse DTOs containing detailed movie cast information.
    public static List<MovieCastDetailsResponse> mapToMovieCastDetailsResponse(List<MovieCast> movieCast) {
        return movieCast.stream()
                        .map(MovieCastMapper::mapToMovieCastDetailsResponse)
                        .toList();
    }

    /// Maps a MovieCast entity to a MovieCastDetailsResponse DTO.
    ///
    /// @param movieCast The MovieCast entity to map.
    /// @return A MovieCastDetailsResponse DTO containing the movie cast's information.
    private static MovieCastDetailsResponse mapToMovieCastDetailsResponse(MovieCast movieCast) {
        return MovieCastDetailsResponse.builder()
                                       .id(movieCast.getActor().getId())
                                       .name(movieCast.getActor().getName())
                                       .role(movieCast.getRole())
                                       .build();
    }

}
