package com.moviehub.service;

import com.moviehub.entity.CommentInfo;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
import com.moviehub.repository.CommentReactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing reactions to comments.
/// This class provides methods for adding, updating, and retrieving reactions to comments.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CommentInfoReactionService {

    /// Repository for managing CommentReaction entities.
    private final CommentReactionRepository reactionRepository;

    /// Adds a reaction for a specific comment by a user.
    /// If the user has already reacted, the existing reaction is updated.
    /// If the user is reacting for the first time, a new reaction is created.
    ///
    /// @param commentInfo The comment info to which the reaction is added.
    /// @param reactionType The type of reaction (like, dislike, etc.).
    /// @param user The user adding the reaction.
    public void addCommentInfoReaction(CommentInfo commentInfo, ReactionType reactionType, User user) {
        log.info("adding reaction: {} for comment info: {}", reactionType, commentInfo.getId());

        Optional<CommentReaction> existingReaction = reactionRepository.findByCommentInfoIdAndUserId(commentInfo.getId(), user.getId());

        existingReaction.ifPresentOrElse(
            // user has already reacted to the comment -> update the reaction
            reaction -> updateExistingReaction(reaction, commentInfo, reactionType),
            // user has reacted for the first time -> create new reaction
            () -> saveNewReaction(commentInfo, user, reactionType)
        );
    }

    /// Updates an existing reaction for a comment.
    /// Adjusts the reaction counts accordingly based on the new reaction type.
    ///
    /// @param reaction The existing CommentReaction to update.
    /// @param commentInfo The associated CommentInfo.
    /// @param newReaction The new reaction type to apply.
    private void updateExistingReaction(CommentReaction reaction, CommentInfo commentInfo, ReactionType newReaction) {
        log.debug("updating existing reaction: {}, with new reaction: {}", reaction.getReactionType(), newReaction);

        updateCommentInfoReactions(commentInfo, reaction.getReactionType(), newReaction);

        // user wants to delete their reaction
        if (newReaction == ReactionType.NONE) {
            reactionRepository.delete(reaction);
        }
        else {
            reaction.setReactionType(newReaction);
            reactionRepository.save(reaction);
        }
    }

    /// Updates the reaction counts for a comment based on the previous and new reactions.
    ///
    /// @param commentInfo The CommentInfo to update.
    /// @param previousReaction The previous reaction type.
    /// @param newReaction The new reaction type.
    private void updateCommentInfoReactions(CommentInfo commentInfo, ReactionType previousReaction, ReactionType newReaction) {
        if (previousReaction == newReaction) {
            return;
        }

        log.debug("updating reaction count for comment info: {}", commentInfo.getId());

        // decrement old reaction count
        adjustReactionCount(commentInfo, previousReaction, -1);
        // increment new reaction count
        adjustReactionCount(commentInfo, newReaction, 1);
    }

    /// Adjusts the reaction count for a comment based on the reaction type and adjustment value.
    ///
    /// @param commentInfo The CommentInfo to update.
    /// @param reaction The ReactionType for which the count is adjusted.
    /// @param adjustment The adjustment value (positive or negative).
    private void adjustReactionCount(CommentInfo commentInfo, ReactionType reaction, int adjustment) {
        if (reaction == ReactionType.LIKE) {
            commentInfo.setLikes(commentInfo.getLikes() + adjustment);
        }
        else if (reaction == ReactionType.DISLIKE) {
            commentInfo.setDislikes(commentInfo.getDislikes() + adjustment);
        }
    }

    /// Saves a new reaction for a comment by a user.
    /// Updates the reaction counts accordingly.
    ///
    /// @param commentInfo The CommentInfo to which the reaction is added.
    /// @param user The user adding the reaction.
    /// @param reactionType The type of reaction (like, dislike, etc.).
    private void saveNewReaction(CommentInfo commentInfo, User user, ReactionType reactionType) {
        log.debug("saving new reaction: {} for comment info: {}", reactionType, commentInfo.getId());

        CommentReaction reaction = CommentReaction.builder()
                                                  .commentInfo(commentInfo)
                                                  .user(user)
                                                  .reactionType(reactionType)
                                                  .build();

        reactionRepository.save(reaction);

        updateCommentInfoReactions(commentInfo, ReactionType.NONE, reactionType);
    }

    /// Retrieves a list of reactions made by a user for specified comments.
    ///
    /// @param userId The ID of the user whose reactions are retrieved.
    /// @param commentIds The list of comment IDs for which to fetch reactions.
    /// @return A list of CommentReaction objects.
    public List<CommentReaction> getUserReactionsForComments(String userId, List<UUID> commentIds) {
        return reactionRepository.findUserReactionsByCommentIds(userId, commentIds);
    }

}
