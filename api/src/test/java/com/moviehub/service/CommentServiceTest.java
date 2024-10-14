package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ReactionType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentReactionService reactionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;

    private static final UUID COMMENT_ID = UUID.fromString("3cc3fb2a-8911-42cd-acf6-d90795154324");
    private static final UUID PARENT_COMMENT_ID = UUID.fromString("4098a00c-b0f8-428e-ae74-660e2cf7948c");

    private static final String FIRST_COMMENT_TEXT = "Comment 1";
    private static final String SECOND_COMMENT_TEXT = "Comment 2";
    private static final String DELETED_COMMENT_TEXT = "Comment deleted.";

    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    @Test
    void shouldGetCommentWhenCommentExists() {
        Comment comment = createComment(FIRST_COMMENT_TEXT);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getComment(COMMENT_ID);

        assertThat(foundComment.getId()).isEqualTo(COMMENT_ID);
    }

    @Test
    void shouldThrowCommentNotFoundExceptionWhenCommentDoesNotExist() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CommentNotFoundException.class)
            .isThrownBy(() -> commentService.getComment(COMMENT_ID));
    }

    @Test
    void shouldSaveCommentWithValidParentComment() {
        Movie movie = new Movie();
        Comment comment = createComment(FIRST_COMMENT_TEXT);
        Comment parentComment = createComment(SECOND_COMMENT_TEXT);

        when(commentRepository.existsById(PARENT_COMMENT_ID)).thenReturn(true);
        when(userService.getUser()).thenReturn(new User());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentRepository.findById(PARENT_COMMENT_ID)).thenReturn(Optional.of(parentComment));

        Comment savedComment = commentService.saveComment(movie, comment, PARENT_COMMENT_ID);

        assertThat(savedComment.getText()).isEqualTo(comment.getText());
        assertThat(savedComment.getParentComment()).isEqualTo(parentComment);
    }

    @Test
    void shouldSaveCommentWithNoParentComment() {
        Movie movie = new Movie();
        Comment comment = createComment(FIRST_COMMENT_TEXT);

        when(userService.getUser()).thenReturn(new User());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.saveComment(movie, comment, null);

        assertThat(savedComment.getText()).isEqualTo(comment.getText());
        assertThat(savedComment.getParentComment()).isNull();
    }

    @Test
    void shouldThrowCommentNotFoundExceptionWhenParentCommentDoesNotExist() {
        Movie movie = new Movie();
        Comment comment = createComment(FIRST_COMMENT_TEXT);
        when(commentRepository.existsById(PARENT_COMMENT_ID)).thenReturn(false);

        assertThatExceptionOfType(CommentNotFoundException.class)
            .isThrownBy(() -> commentService.saveComment(movie, comment, PARENT_COMMENT_ID));
    }

    @Test
    void shouldDeleteComment() {
        Comment comment = createComment(FIRST_COMMENT_TEXT);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        commentService.deleteComment(COMMENT_ID);

        assertThat(comment.getIsDeleted()).isTrue();
        assertThat(comment.getText()).isEqualTo(DELETED_COMMENT_TEXT);
    }

    @Test
    void shouldAddCommentReaction() {
        Comment comment = createComment(FIRST_COMMENT_TEXT);
        ReactionType reactionType = ReactionType.LIKE;

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        commentService.addCommentReaction(COMMENT_ID, reactionType);

        verify(reactionService).addCommentReaction(comment, reactionType);
    }

    @Test
    void shouldGetComments() {
        Movie movie = new Movie();
        Comment comment1 = createComment(FIRST_COMMENT_TEXT);
        Comment comment2 = createComment(SECOND_COMMENT_TEXT);
        Page<Comment> commentPage = new PageImpl<>(List.of(comment1, comment2));

        when(commentRepository.findAllTopLevelComments(movie, PAGEABLE)).thenReturn(commentPage);
        when(userService.getUser()).thenReturn(new User());

        Page<Comment> resultPage = commentService.getComments(movie, PAGEABLE);

        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent()).containsExactlyInAnyOrder(comment1, comment2);
    }

    @Test
    void shouldGetCommentsWhenUserIsNull() {
        Movie movie = new Movie();
        Comment comment1 = createComment(FIRST_COMMENT_TEXT);
        Comment comment2 = createComment(SECOND_COMMENT_TEXT);
        Page<Comment> commentPage = new PageImpl<>(List.of(comment1, comment2));

        when(commentRepository.findAllTopLevelComments(movie, PAGEABLE)).thenReturn(commentPage);
        when(userService.getUser()).thenReturn(null);

        Page<Comment> resultPage = commentService.getComments(movie, PAGEABLE);

        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent()).containsExactlyInAnyOrder(comment1, comment2);
    }

    private Comment createComment(String text) {
        User user = createUser();

        Comment comment = Comment.builder()
                                 .id(COMMENT_ID)
                                 .text(text)
                                 .isDeleted(false)
                                 .user(user)
                                 .reactions(List.of())
                                 .build();

        comment.setReactions(List.of(createCommentReaction(comment, user)));

        return comment;
    }

    private User createUser() {
        return User.builder()
                   .id("auth0|1")
                   .name("Arnold")
                   .build();
    }

    private CommentReaction createCommentReaction(Comment comment, User user) {
        return CommentReaction.builder()
                              .comment(comment)
                              .user(user)
                              .reactionType(ReactionType.LIKE)
                              .build();
    }

}