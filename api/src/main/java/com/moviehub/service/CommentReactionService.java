package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.ReactionType;
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
        Optional<CommentReaction> existingReaction = reactionRepository.findByCommentAndUser(comment, userService.getUser());

        // user has already reacted to the comment -> update the reaction
        if (existingReaction.isPresent()) {
            updateExistingReaction(existingReaction.get(), comment, reactionType);
            return;
        }

        createNewReaction(comment, reactionType);
    }

    private void updateExistingReaction(CommentReaction reaction, Comment comment, ReactionType newReactionType) {
        ReactionType previousReactionType = reaction.getReactionType();

        updateCommentReactions(comment, previousReactionType, newReactionType);

        // user wants to delete their reaction
        if (newReactionType == ReactionType.NONE) {
            reactionRepository.delete(reaction);
            return;
        }

        reaction.setReactionType(newReactionType);
        reactionRepository.save(reaction);
    }

    private void updateCommentReactions(Comment comment, ReactionType previousReactionType, ReactionType newReactionType) {
        if (previousReactionType == newReactionType) {
            return;
        }

        switch (previousReactionType) {
            case LIKE -> comment.setLikes(comment.getLikes() - 1);
            case DISLIKE -> comment.setDislikes(comment.getDislikes() - 1);
            case NONE -> {}
        }

        switch (newReactionType) {
            case LIKE -> comment.setLikes(comment.getLikes() + 1);
            case DISLIKE -> comment.setDislikes(comment.getDislikes() + 1);
            case NONE -> {}
        }
    }

    private void createNewReaction(Comment comment, ReactionType reactionType) {
        CommentReaction reaction = CommentReaction.builder()
                                                  .comment(comment)
                                                  .user(userService.getUser())
                                                  .reactionType(reactionType)
                                                  .build();

        reactionRepository.save(reaction);
    }

}
