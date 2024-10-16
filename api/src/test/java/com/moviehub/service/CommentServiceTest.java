package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.User;
import com.moviehub.exception.CommentNotFoundException;
import com.moviehub.repository.CommentRepository;
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
import java.util.Optional;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createComment;
import static com.moviehub.EntityBuilder.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private static final UUID COMMENT_ID = UUID.fromString("3cc3fb2a-8911-42cd-acf6-d90795154324");
    private static final UUID MOVIE_ID = UUID.fromString("7ba1d5f0-ff31-4ba3-8d77-5187e03f73ea");

    private static final String COMMENT_TEXT = "Comment 1";
    private static final String DELETED_COMMENT_TEXT = "Comment deleted.";

    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    @Test
    void shouldGetCommentWhenCommentExists() {
        Comment comment = (Comment) createComment(null, null, COMMENT_TEXT);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getComment(COMMENT_ID);

        assertThat(foundComment.getText()).isEqualTo(COMMENT_TEXT);
    }

    @Test
    void shouldThrowCommentNotFoundExceptionWhenCommentDoesNotExist() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CommentNotFoundException.class)
            .isThrownBy(() -> commentService.getComment(COMMENT_ID));
    }

    @Test
    void shouldAddComment() {
        Movie movie = new Movie();
        User user = createUser("John");
        String commentText = "New comment";

        commentService.addComment(movie, commentText, user);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void shouldDeleteComment() {
        User user = createUser("John");
        Comment comment = (Comment) createComment(null, null, COMMENT_TEXT);

        when(commentRepository.findByIdAndUser(COMMENT_ID, user)).thenReturn(Optional.of(comment));

        commentService.deleteComment(COMMENT_ID, user);

        assertThat(comment.getIsDeleted()).isTrue();
        assertThat(comment.getText()).isEqualTo(DELETED_COMMENT_TEXT);
        verify(commentRepository).save(comment);
    }

    @Test
    void shouldThrowCommentNotFoundExceptionWhenDeletingNonExistentComment() {
        User user = createUser("John");

        when(commentRepository.findByIdAndUser(COMMENT_ID, user)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CommentNotFoundException.class)
            .isThrownBy(() -> commentService.deleteComment(COMMENT_ID, user));
    }

    @Test
    void shouldGetComments() {
        Comment comment1 = (Comment) createComment(null, null, COMMENT_TEXT);
        Comment comment2 = (Comment) createComment(null, null, "Another comment");
        Page<Comment> commentPage = new PageImpl<>(List.of(comment1, comment2));

        when(commentRepository.findCommentsByMovieId(MOVIE_ID, PAGEABLE)).thenReturn(commentPage);

        Page<Comment> resultPage = commentService.getComments(MOVIE_ID, PAGEABLE);

        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent()).containsExactlyInAnyOrder(comment1, comment2);
    }

    @Test
    void shouldGetCommentById() {
        Comment comment = (Comment) createComment(null, null, COMMENT_TEXT);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.getCommentById(COMMENT_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getText()).isEqualTo(COMMENT_TEXT);
    }

}