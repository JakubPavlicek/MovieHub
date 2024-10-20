package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.User;
import com.moviehub.exception.CommentNotFoundException;
import com.moviehub.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private static final String COMMENT_DELETED = "Comment deleted.";

    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    public Comment getCommentByUser(UUID commentId, User user) {
        return commentRepository.findByIdAndUser(commentId, user)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    public Page<Comment> getComments(UUID movieId, Pageable pageable) {
        log.info("fetching comments for movie: {}", movieId);

        return commentRepository.findCommentsByMovieId(movieId, pageable);
    }

    public void addComment(Movie movie, String text, User user) {
        log.info("adding comment to movie: {}", movie.getId());

        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText(text);

        commentRepository.save(comment);
    }

    public void deleteComment(UUID commentId, User user) {
        Comment comment = getCommentByUser(commentId, user);

        comment.setText(COMMENT_DELETED);
        comment.setIsDeleted(true);

        commentRepository.save(comment);
    }

}
