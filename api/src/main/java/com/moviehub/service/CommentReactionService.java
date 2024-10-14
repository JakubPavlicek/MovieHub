package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
import com.moviehub.repository.CommentReactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository reactionRepository;

    private final UserService userService;

    public void addCommentReaction(Comment comment, ReactionType reactionType) {
        User user = userService.getUser();
        Optional<CommentReaction> existingReaction = reactionRepository.findByCommentAndUser(comment, user);

        existingReaction.ifPresentOrElse(
            // user has already reacted to the comment -> update the reaction
            reaction -> updateExistingReaction(reaction, comment, reactionType),
            // user has reacted for the first time -> create new reaction
            () -> saveNewReaction(comment, user, reactionType)
        );
    }

    private void updateExistingReaction(CommentReaction reaction, Comment comment, ReactionType newReaction) {
        updateCommentReactions(comment, reaction.getReactionType(), newReaction);

        // user wants to delete their reaction
        if (newReaction == ReactionType.NONE) {
            reactionRepository.delete(reaction);
        }
        else {
            reaction.setReactionType(newReaction);
            reactionRepository.save(reaction);
        }
    }

    private void updateCommentReactions(Comment comment, ReactionType previousReaction, ReactionType newReaction) {
        if (previousReaction == newReaction) {
            return;
        }

        // decrement old reaction count
        adjustReactionCount(comment, previousReaction, -1);
        // increment new reaction count
        adjustReactionCount(comment, newReaction, 1);
    }

    private void adjustReactionCount(Comment comment, ReactionType reaction, int adjustment) {
        if (reaction == ReactionType.LIKE) {
            comment.setLikes(comment.getLikes() + adjustment);
        }
        else if (reaction == ReactionType.DISLIKE) {
            comment.setDislikes(comment.getDislikes() + adjustment);
        }
    }

    private void saveNewReaction(Comment comment, User user, ReactionType reactionType) {
        CommentReaction reaction = CommentReaction.builder()
                                                  .comment(comment)
                                                  .user(user)
                                                  .reactionType(reactionType)
                                                  .build();

        reactionRepository.save(reaction);

        updateCommentReactions(comment, ReactionType.NONE, reactionType);
    }

}
