package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentInfo;
import com.moviehub.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
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

import static com.moviehub.EntityBuilder.createComment;
import static com.moviehub.EntityBuilder.createMovie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieInteractionServiceTest {

    @Mock
    private CommentInfoService commentInfoService;

    @Mock
    private MovieRatingService ratingService;

    @Mock
    private ParseService parseService;

    @InjectMocks
    private MovieInteractionService movieInteractionService;

    private static final String COMMENT_TEXT = "Great movie!";
    private static final String SORT = "createdDate,desc";
    private static final Integer PAGE = 0;
    private static final Integer LIMIT = 10;
    private static final Double RATING = 8.5;

    private static Movie movie;

    @BeforeEach
    void setUp() {
        movie = createMovie("Movie");
    }

    @Test
    void shouldSaveComment() {
        CommentInfo comment = createComment(movie, null, COMMENT_TEXT);

        movieInteractionService.addComment(movie, comment.getText());

        verify(commentInfoService, times(1)).addComment(movie, comment.getText());
    }

    @Test
    void shouldGetComments() {
        Comment comment = (Comment) createComment(movie, null, COMMENT_TEXT);
        Pageable pageable = PageRequest.of(PAGE, LIMIT);
        Page<Comment> commentPage = new PageImpl<>(List.of(comment), pageable, 1);

        when(parseService.parseCommentSort(SORT)).thenReturn(pageable.getSort());
        when(commentInfoService.getComments(movie.getId(), pageable)).thenReturn(commentPage);

        Page<Comment> result = movieInteractionService.getComments(movie.getId(), PAGE, LIMIT, SORT);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).containsExactly(comment);
    }

    @Test
    void shouldSaveRating() {
        when(ratingService.saveRating(movie, RATING)).thenReturn(true);

        boolean result = movieInteractionService.saveRating(movie, RATING);

        assertThat(result).isTrue();
    }

    @Test
    void shouldCalculateRating() {
        when(ratingService.calculateRating(any())).thenReturn(RATING);

        Double result = movieInteractionService.calculateRating(movie.getId());

        assertThat(result).isEqualTo(RATING);
    }

    @Test
    void shouldGetRating() {
        when(ratingService.getRating(movie)).thenReturn(RATING);

        Double result = movieInteractionService.getRating(movie);

        assertThat(result).isEqualTo(RATING);
    }

}