package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.Movie;
import com.moviehub.exception.CommentNotFoundException;
import com.moviehub.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentInfoReactionService reactionService;
    private final UserService userService;

    private static final String COMMENT_DELETED = "Comment deleted.";

    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    public void addComment(Movie movie, String text) {
        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(userService.getUser());
        comment.setText(text);

        commentRepository.save(comment);
    }

    public Page<Comment> getComments(UUID movieId, Pageable pageable) {
        log.info("fetching comments for movie with ID: {}", movieId);

        return commentRepository.findAllCommentsWithReplies(movieId, pageable);
    }

    public void deleteComment(UUID commentId) {
        Comment comment = getComment(commentId);

        comment.setText(COMMENT_DELETED);
        comment.setIsDeleted(true);

        commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(UUID commentId) {
        return commentRepository.findById(commentId);
    }

    public Page<CommentReply> getReplies(UUID commentId, Pageable pageable) {
        return commentRepository.findAllReplies(commentId, pageable);
    }

}
