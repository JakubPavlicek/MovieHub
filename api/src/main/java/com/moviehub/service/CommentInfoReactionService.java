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

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CommentInfoReactionService {

    private final CommentReactionRepository reactionRepository;

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

    private void adjustReactionCount(CommentInfo commentInfo, ReactionType reaction, int adjustment) {
        if (reaction == ReactionType.LIKE) {
            commentInfo.setLikes(commentInfo.getLikes() + adjustment);
        }
        else if (reaction == ReactionType.DISLIKE) {
            commentInfo.setDislikes(commentInfo.getDislikes() + adjustment);
        }
    }

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

    public List<CommentReaction> getUserReactionsForComments(String userId, List<UUID> commentIds) {
        return reactionRepository.findUserReactionsByCommentIds(userId, commentIds);
    }

}
