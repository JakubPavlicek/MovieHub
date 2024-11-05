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

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing comments.
/// This class provides methods for adding, retrieving, and deleting comments on movies.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CommentService {

    /// Repository for managing Comment entities.
    private final CommentRepository commentRepository;

    /// Constant message indicating that a comment has been deleted.
    private static final String COMMENT_DELETED = "Comment deleted.";

    /// Retrieves a comment by its ID.
    ///
    /// @param commentId The ID of the comment to retrieve.
    /// @return The Comment associated with the specified ID.
    /// @throws CommentNotFoundException if the comment does not exist.
    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    /// Retrieves a comment by its ID and the associated user.
    ///
    /// @param commentId The ID of the comment to retrieve.
    /// @param user The user associated with the comment.
    /// @return The Comment associated with the specified ID and user.
    /// @throws CommentNotFoundException if the comment does not exist for the specified user.
    public Comment getCommentByUser(UUID commentId, User user) {
        return commentRepository.findByIdAndUser(commentId, user)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    /// Retrieves a paginated list of comments for a specific movie.
    ///
    /// @param movieId The ID of the movie for which to retrieve comments.
    /// @param pageable The pagination information.
    /// @return A paginated list of Comment entities associated with the movie.
    public Page<Comment> getComments(UUID movieId, Pageable pageable) {
        log.info("fetching comments for movie: {}", movieId);

        return commentRepository.findCommentsByMovieId(movieId, pageable);
    }

    /// Adds a comment to a specific movie by the user.
    ///
    /// @param movie The movie to which the comment is added.
    /// @param text The text of the comment.
    /// @param user The user adding the comment.
    public void addComment(Movie movie, String text, User user) {
        log.info("adding comment to movie: {}", movie.getId());

        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText(text);

        commentRepository.save(comment);
    }

    /// Deletes a comment by its ID, marking it as deleted instead of removing it from the database.
    ///
    /// @param commentId The ID of the comment to delete.
    /// @param user The user attempting to delete the comment.
    public void deleteComment(UUID commentId, User user) {
        Comment comment = getCommentByUser(commentId, user);

        comment.setText(COMMENT_DELETED);
        comment.setIsDeleted(true);

        commentRepository.save(comment);
    }

}
