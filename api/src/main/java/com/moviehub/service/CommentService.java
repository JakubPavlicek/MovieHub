package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ReactionType;
import com.moviehub.exception.CommentNotFoundException;
import com.moviehub.repository.CommentRepository;
import com.moviehub.security.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentReactionService reactionService;

    private static final String DELETION_TEXT = "Comment deleted.";

    public Comment getComment(String commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    public Comment saveComment(Movie movie, Comment comment) {
        ensureValidParentCommentId(comment);

        comment.setMovie(movie);
        comment.setUserId(AuthUser.getUserId());

        return commentRepository.save(comment);
    }

    private void ensureValidParentCommentId(Comment comment) {
        if (comment.getParentCommentId() == null) {
            return;
        }

        if (!commentRepository.existsById(comment.getParentCommentId())) {
            throw new CommentNotFoundException("Parent comment with ID: " + comment.getParentCommentId() + " not found");
        }
    }

    public Page<Comment> getComments(Movie movie, Pageable pageable) {
        return commentRepository.findAllByMovie(movie, pageable);
    }

    public void deleteComment(String commentId) {
        Comment comment = getComment(commentId);

        comment.setText(DELETION_TEXT);
        comment.setIsDeleted(true);

        commentRepository.save(comment);
    }

    public void addCommentReaction(String commentId, ReactionType reactionType) {
        Comment comment = getComment(commentId);
        reactionService.addCommentReaction(comment, reactionType);
    }

}
