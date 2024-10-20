package com.moviehub.mapper.dto;

import com.moviehub.dto.ActorDetailsResponse;
import com.moviehub.dto.ActorPage;
import com.moviehub.dto.AddActorRequest;
import com.moviehub.dto.GenderName;
import com.moviehub.dto.UpdateActorRequest;
import com.moviehub.entity.Actor;
import com.moviehub.entity.Gender;
import org.springframework.data.domain.Page;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between Actor entities and Data Transfer Objects (DTOs).
public class ActorMapper {

    // Private constructor to prevent instantiation.
    private ActorMapper() {
    }

    /// Maps an AddActorRequest DTO to an Actor entity.
    ///
    /// @param addActorRequest The DTO containing the actor's details to add.
    /// @return An Actor entity populated with the details from the request.
    public static Actor mapToActor(AddActorRequest addActorRequest) {
        return Actor.builder()
                    .name(addActorRequest.getName())
                    .bio(addActorRequest.getBio())
                    .gender(Gender.valueOf(addActorRequest.getGender().name()))
                    .build();
    }

    /// Maps an UpdateActorRequest DTO to an Actor entity.
    ///
    /// @param updateActorRequest The DTO containing the actor's updated details.
    /// @return An Actor entity populated with the updated details from the request.
    public static Actor mapToActor(UpdateActorRequest updateActorRequest) {
        return Actor.builder()
                    .name(updateActorRequest.getName())
                    .bio(updateActorRequest.getBio())
                    .gender(Gender.valueOf(updateActorRequest.getGender().name()))
                    .build();
    }

    /// Maps an Actor entity to an ActorDetailsResponse DTO.
    ///
    /// @param actor The Actor entity to map.
    /// @return An ActorDetailsResponse DTO containing the actor's details.
    public static ActorDetailsResponse mapToActorDetailsResponse(Actor actor) {
        return ActorDetailsResponse.builder()
                                   .id(actor.getId())
                                   .name(actor.getName())
                                   .bio(actor.getBio())
                                   .gender(GenderName.fromValue(actor.getGender().getValue()))
                                   .build();
    }

    /// Maps a Page of Actor entities to an ActorPage DTO.
    ///
    /// @param actors The page of Actor entities to map.
    /// @return An ActorPage DTO containing the mapped actor details and pagination info.
    public static ActorPage mapToActorPage(Page<Actor> actors) {
        return ActorPage.builder()
                        .content(mapToActorDetailsResponseList(actors))
                        .pageable(PageableMapper.mapToPageableDTO(actors.getPageable()))
                        .last(actors.isLast())
                        .totalElements(actors.getTotalElements())
                        .totalPages(actors.getTotalPages())
                        .first(actors.isFirst())
                        .size(actors.getSize())
                        .number(actors.getNumber())
                        .sort(SortMapper.mapToSortDTO(actors.getSort()))
                        .numberOfElements(actors.getNumberOfElements())
                        .empty(actors.isEmpty())
                        .build();
    }

    /// Maps a Page of Actor entities to a list of ActorDetailsResponse DTOs.
    ///
    /// @param actors The page of Actor entities to map.
    /// @return A list of ActorDetailsResponse DTOs.
    private static List<ActorDetailsResponse> mapToActorDetailsResponseList(Page<Actor> actors) {
        return actors.stream()
                     .map(ActorMapper::mapToActorDetailsResponse)
                     .toList();
    }

}
