package com.movie_manager.mapper.dto;

import com.movie_manager.dto.ActorDetailsResponse;
import com.movie_manager.dto.ActorPage;
import com.movie_manager.dto.AddActorRequest;
import com.movie_manager.dto.GenderName;
import com.movie_manager.dto.UpdateActorRequest;
import com.movie_manager.entity.Actor;
import org.springframework.data.domain.Page;

import java.util.List;

public class ActorMapper {

    private ActorMapper() {
    }

    public static Actor mapToActor(AddActorRequest addActorRequest) {
        return Actor.builder()
                    .name(addActorRequest.getName())
                    .bio(addActorRequest.getBio())
                    .gender(GenderMapper.mapToGender(addActorRequest.getGender()))
                    .build();
    }

    public static Actor mapToActor(UpdateActorRequest updateActorRequest) {
        return Actor.builder()
                    .name(updateActorRequest.getName())
                    .bio(updateActorRequest.getBio())
                    .gender(GenderMapper.mapToGender(updateActorRequest.getGender()))
                    .build();
    }

    public static ActorDetailsResponse mapToActorDetailsResponse(Actor actor) {
        return ActorDetailsResponse.builder()
                                   .actorId(actor.getActorId())
                                   .name(actor.getName())
                                   .bio(actor.getBio())
                                   .gender(GenderName.fromValue(actor.getGender().getName()))
                                   .build();
    }

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

    private static List<ActorDetailsResponse> mapToActorDetailsResponseList(Page<Actor> actors) {
        return actors.stream()
                     .map(ActorMapper::mapToActorDetailsResponse)
                     .toList();
    }

}
