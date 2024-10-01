package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.ReactionType;
import com.moviehub.repository.CommentReactionRepository;
import com.moviehub.security.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository reactionRepository;

    public void addCommentReaction(Comment comment, ReactionType reactionType) {
        // user wants to delete the reaction
        if (reactionType.equals(ReactionType.NONE)) {
            reactionRepository.removeByCommentAndUserId(comment, AuthUser.getUserId());
            return;
        }

        Optional<CommentReaction> existingReaction = reactionRepository.findByCommentAndUserId(comment, AuthUser.getUserId());

        // user has already reacted to the comment -> update the reaction
        if (existingReaction.isPresent()) {
            updateExistingReaction(existingReaction.get(), reactionType);
            return;
        }

        createNewReaction(comment, reactionType);
    }

    private void updateExistingReaction(CommentReaction commentReaction, ReactionType reactionType) {
        commentReaction.setReactionType(reactionType);
        reactionRepository.save(commentReaction);
    }

    private void createNewReaction(Comment comment, ReactionType reactionType) {
        CommentReaction reaction = CommentReaction.builder()
                                                  .comment(comment)
                                                  .userId(AuthUser.getUserId())
                                                  .reactionType(reactionType)
                                                  .build();

        reactionRepository.save(reaction);
    }

}
