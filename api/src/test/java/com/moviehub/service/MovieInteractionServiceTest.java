package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieInteractionServiceTest {

    @Mock
    private CommentService commentService;

    @Mock
    private MovieRatingService ratingService;

    @Mock
    private ParseService parseService;

    @InjectMocks
    private MovieInteractionService movieInteractionService;

    private static final UUID MOVIE_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID COMMENT_ID = UUID.fromString("456e7890-e89b-12d3-a456-426614174001");
    private static final String COMMENT_TEXT = "Great movie!";
    private static final Double RATING = 8.5;
    private static final Integer PAGE = 0;
    private static final Integer LIMIT = 10;
    private static final String SORT = "createdDate,desc";

    @Test
    void shouldSaveComment() {
        Movie movie = createMovie();
        Comment comment = createComment();

        movieInteractionService.saveComment(movie, comment, null);

        verify(commentService, times(1)).saveComment(movie, comment, null);
    }

    @Test
    void shouldGetComments() {
        Movie movie = createMovie();
        Comment comment = createComment();
        Pageable pageable = PageRequest.of(PAGE, LIMIT);
        Page<Comment> expectedPage = new PageImpl<>(List.of(comment), pageable, 1);

        when(parseService.parseCommentSort(SORT)).thenReturn(pageable.getSort());
        when(commentService.getComments(movie, pageable)).thenReturn(expectedPage);

        Page<Comment> actualPage = movieInteractionService.getComments(movie, PAGE, LIMIT, SORT);

        assertThat(actualPage).isNotNull();
        assertThat(actualPage.getTotalElements()).isEqualTo(1);
        assertThat(actualPage.getContent()).containsExactly(comment);
    }

    @Test
    void shouldSaveRating() {
        Movie movie = createMovie();

        when(ratingService.saveRating(movie, RATING)).thenReturn(true);

        boolean result = movieInteractionService.saveRating(movie, RATING);

        assertThat(result).isTrue();
    }

    @Test
    void shouldCalculateRating() {
        when(ratingService.calculateRating(MOVIE_ID)).thenReturn(RATING);

        Double result = movieInteractionService.calculateRating(MOVIE_ID);

        assertThat(result).isEqualTo(RATING);
    }

    @Test
    void shouldGetRating() {
        Movie movie = createMovie();

        when(ratingService.getRating(movie)).thenReturn(RATING);

        Double result = movieInteractionService.getRating(movie);

        assertThat(result).isEqualTo(RATING);
    }

    private static Movie createMovie() {
        return Movie.builder()
                    .id(MOVIE_ID)
                    .build();
    }

    private static Comment createComment() {
        return Comment.builder()
                      .id(COMMENT_ID)
                      .text(COMMENT_TEXT)
                      .build();
    }

}