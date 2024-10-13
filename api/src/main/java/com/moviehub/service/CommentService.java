package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
import com.moviehub.exception.CommentNotFoundException;
import com.moviehub.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentReactionService reactionService;

    private final UserService userService;

    private static final String COMMENT_DELETED = "Comment deleted.";

    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    public Comment saveComment(Movie movie, Comment comment, UUID parentCommentId) {
        ensureValidParentCommentId(parentCommentId);

        comment.setMovie(movie);
        comment.setUser(userService.getUser());
        comment.setParentComment(parentCommentId == null ? null : getComment(parentCommentId));

        return commentRepository.save(comment);
    }

    private void ensureValidParentCommentId(UUID parentCommentId) {
        if (parentCommentId == null) {
            return;
        }

        if (!commentRepository.existsById(parentCommentId)) {
            throw new CommentNotFoundException("Parent comment with ID: " + parentCommentId + " not found");
        }
    }

    public Page<Comment> getComments(Movie movie, Pageable pageable) {
        Page<Comment> topLevelComments = commentRepository.findAllTopLevelComments(movie, pageable);

        User authUser = userService.getUser();

        // set transient fields for top-level comments and their replies
        topLevelComments.forEach(comment -> {
            setTransientFields(comment, authUser);
            comment.getReplies().forEach(reply -> setTransientFields(reply, authUser));
        });

        return topLevelComments;
    }

    private void setTransientFields(Comment comment, User user) {
        if (user == null) {
            return;
        }

        comment.setUserReaction(user);
        comment.setAuthorFlag(user);
    }

    public void deleteComment(UUID commentId) {
        Comment comment = getComment(commentId);

        comment.setText(COMMENT_DELETED);
        comment.setIsDeleted(true);

        commentRepository.save(comment);
    }

    public void addCommentReaction(UUID commentId, ReactionType reactionType) {
        Comment comment = getComment(commentId);
        reactionService.addCommentReaction(comment, reactionType);
    }

}
