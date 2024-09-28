package com.moviehub.mapper.dto;

import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.entity.ReactionType;

public class ReactionTypeMapper {

    private ReactionTypeMapper() {
    }

    public static ReactionType mapToReactionType(ReactionTypeRequest reactionTypeRequest) {
        return ReactionType.valueOf(reactionTypeRequest.getReactionType().name());
    }

}
