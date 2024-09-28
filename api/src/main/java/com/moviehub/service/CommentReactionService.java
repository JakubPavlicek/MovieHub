package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.ReactionType;
import com.moviehub.repository.CommentReactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository reactionRepository;

    public void addCommentReaction(Comment comment, String reactionType) {
        ensureUniqueReaction(comment, "7d63632f-792a-4691-b7d3-7d9096afba30");

        CommentReaction reaction = CommentReaction.builder()
                                                  .comment(comment)
                                                  .userId("7d63632f-792a-4691-b7d3-7d9096afba30")
                                                  .reactionType(ReactionType.valueOf(reactionType.toUpperCase()))
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
