package com.moviehub.mapper.dto;

import com.moviehub.dto.Reaction;
import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.entity.ReactionType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ReactionTypeMapperTest {

    private static Stream<Arguments> reactionToReactionType() {
        return Stream.of(
            Arguments.of(Reaction.LIKE, ReactionType.LIKE),
            Arguments.of(Reaction.DISLIKE, ReactionType.DISLIKE),
            Arguments.of(Reaction.NONE, ReactionType.NONE)
        );
    }

    private static Stream<Arguments> reactionTypeToReaction() {
        return Stream.of(
            Arguments.of(ReactionType.LIKE, Reaction.LIKE),
            Arguments.of(ReactionType.DISLIKE, Reaction.DISLIKE),
            Arguments.of(ReactionType.NONE, Reaction.NONE)
        );
    }

    @ParameterizedTest
    @MethodSource("reactionToReactionType")
    void shouldMapReactionTypeRequestToReactionType(Reaction reaction, ReactionType reactionType) {
        ReactionTypeRequest reactionTypeRequest = ReactionTypeRequest.builder()
                                                                     .reactionType(reaction)
                                                                     .build();

        ReactionType actualReaction = ReactionTypeMapper.mapToReactionType(reactionTypeRequest);

        assertThat(actualReaction).isEqualTo(reactionType);
    }

    @ParameterizedTest
    @MethodSource("reactionTypeToReaction")
    void shouldMapReactionTypeToReaction(ReactionType reactionType, Reaction reaction) {
        Reaction actualReaction = ReactionTypeMapper.mapToReactionType(reactionType);

        assertThat(actualReaction).isEqualTo(reaction);
    }

}