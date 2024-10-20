package com.moviehub.mapper.dto;

import com.moviehub.dto.Reaction;
import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.entity.ReactionType;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between ReactionType entities and Reaction DTOs.
public class ReactionTypeMapper {

    // Private constructor to prevent instantiation.
    private ReactionTypeMapper() {
    }

    /// Maps a ReactionTypeRequest DTO to a ReactionType entity.
    ///
    /// @param reactionTypeRequest The ReactionTypeRequest DTO to map.
    /// @return The corresponding ReactionType entity.
    public static ReactionType mapToReactionType(ReactionTypeRequest reactionTypeRequest) {
        return ReactionType.valueOf(reactionTypeRequest.getReactionType()
                                                       .name());
    }

    /// Maps a ReactionType entity to a Reaction DTO.
    ///
    /// @param reactionType The ReactionType entity to map.
    /// @return The corresponding Reaction DTO.
    public static Reaction mapToReactionType(ReactionType reactionType) {
        return Reaction.valueOf(reactionType.name());
    }

}
