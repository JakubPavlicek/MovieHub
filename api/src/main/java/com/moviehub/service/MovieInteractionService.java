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

@Service
@Transactional
@RequiredArgsConstructor
public class MovieInteractionService {

    private final CommentInfoService commentInfoService;
    private final MovieRatingService ratingService;
    private final ParseService parseService;

    public void addComment(Movie movie, String text) {
        commentInfoService.addComment(movie, text);
    }

    public Page<Comment> getComments(UUID movieId, Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseCommentSort(sort));
        return commentInfoService.getComments(movieId, pageable);
    }

    public boolean saveRating(Movie movie, Double rating) {
        return ratingService.saveRating(movie, rating);
    }

    public Double calculateRating(UUID movieId) {
        return ratingService.calculateRating(movieId);
    }

    public Double getRating(Movie movie) {
        return ratingService.getRating(movie);
    }

}
