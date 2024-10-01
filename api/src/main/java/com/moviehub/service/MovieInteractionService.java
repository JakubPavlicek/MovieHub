package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieRating;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieInteractionService {

    private final CommentService commentService;
    private final MovieRatingService ratingService;
    private final ParseService parseService;

    public Comment saveComment(Movie movie, Comment comment) {
        return commentService.saveComment(movie, comment);
    }

    public Page<Comment> getComments(Movie movie, Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseCommentSort(sort));
        return commentService.getComments(movie, pageable);
    }

    public MovieRating saveRating(Movie movie, Double rating) {
        return ratingService.saveRating(movie, rating);
    }

    public Double calculateRating(UUID movieId) {
        return ratingService.calculateRating(movieId);
    }

}
