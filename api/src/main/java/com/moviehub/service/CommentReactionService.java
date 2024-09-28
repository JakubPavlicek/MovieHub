package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.ReactionType;
import com.moviehub.repository.CommentReactionRepository;
import com.moviehub.security.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        ensureUniqueReaction(comment, AuthUser.getUserId());

        CommentReaction reaction = CommentReaction.builder()
                                                  .comment(comment)
                                                  .userId(AuthUser.getUserId())
                                                  .reactionType(reactionType)
                                                  .build();

        reactionRepository.save(reaction);
    }

    private void ensureUniqueReaction(Comment comment, String userId) {
        // user has already reacted to the comment -> remove the reaction
        if (reactionRepository.existsByCommentAndUserId(comment, userId)) {
            reactionRepository.removeByCommentAndUserId(comment, userId);
        }
    }

}
