package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing interactions with movies, including adding comments
/// and handling movie ratings. This class provides methods for adding comments,
/// retrieving comments for a specific movie, saving ratings, and calculating average ratings for movies.
@Service
@Transactional
@RequiredArgsConstructor
public class MovieInteractionService {

    /// Service for managing comment-related operations.
    private final CommentInfoService commentInfoService;
    /// Service for managing movie rating-related operations.
    private final MovieRatingService ratingService;
    /// Service for parsing sorting options for comments.
    private final ParseService parseService;

    /// Adds a comment to a specified movie.
    ///
    /// @param movie The Movie entity to which the comment will be added.
    /// @param text The text of the comment.
    public void addComment(Movie movie, String text) {
        commentInfoService.addComment(movie, text);
    }

    /// Retrieves a paginated list of comments for a specific movie.
    ///
    /// @param movieId The UUID of the Movie for which comments are to be retrieved.
    /// @param page The page number to retrieve.
    /// @param limit The maximum number of comments to retrieve per page.
    /// @param sort The sorting criteria for the comments.
    /// @return A page of comments for the specified movie.
    public Page<Comment> getComments(UUID movieId, Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseCommentSort(sort));
        return commentInfoService.getComments(movieId, pageable);
    }

    /// Saves a rating for a specified movie.
    ///
    /// @param movie The Movie entity for which the rating will be saved.
    /// @param rating The rating value to save.
    /// @return true if the rating was successfully saved; false otherwise.
    public boolean saveRating(Movie movie, Double rating) {
        return ratingService.saveRating(movie, rating);
    }

    /// Calculates the average rating for a specific movie.
    ///
    /// @param movieId The UUID of the Movie for which the average rating is to be calculated.
    /// @return The average rating of the specified movie.
    public Double calculateRating(UUID movieId) {
        return ratingService.calculateRating(movieId);
    }

    /// Retrieves the current rating for a specified movie.
    ///
    /// @param movie The Movie entity for which the rating will be retrieved.
    /// @return The current rating of the specified movie.
    public Double getRating(Movie movie) {
        return ratingService.getRating(movie);
    }

}
