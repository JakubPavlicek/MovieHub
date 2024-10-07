package com.moviehub.dto;

import com.moviehub.entity.ReactionType;

import java.util.UUID;

public record UserCommentReaction(
    UUID commentId,
    ReactionType reactionType
) {

}
