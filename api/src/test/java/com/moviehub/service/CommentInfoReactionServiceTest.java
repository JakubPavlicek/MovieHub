package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
import com.moviehub.repository.CommentReactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentInfoReactionServiceTest {

    @Mock
    private CommentReactionRepository reactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentInfoReactionService reactionService;

    private static User user;
    private static Comment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        comment = new Comment();
        comment.setLikes(10L);
        comment.setDislikes(5L);

        when(userService.getUser()).thenReturn(user);
    }

    @Test
    void shouldAddNewLikeReaction() {
        when(reactionRepository.findByCommentAndUser(comment, user)).thenReturn(Optional.empty());

        reactionService.addCommentInfoReaction(comment, ReactionType.LIKE);

        verify(reactionRepository).save(any(CommentReaction.class));
        assertThat(comment.getLikes()).isEqualTo(11);
        assertThat(comment.getDislikes()).isEqualTo(5);
    }

    @Test
    void shouldUpdateExistingReactionToDislike() {
        CommentReaction existingReaction = createLikeReaction();
        when(reactionRepository.findByCommentAndUser(comment, user)).thenReturn(Optional.of(existingReaction));

        reactionService.addCommentInfoReaction(comment, ReactionType.DISLIKE);

        verify(reactionRepository).save(existingReaction);
        assertThat(existingReaction.getReactionType()).isEqualTo(ReactionType.DISLIKE);
        assertThat(comment.getLikes()).isEqualTo(9);
        assertThat(comment.getDislikes()).isEqualTo(6);
    }

    @Test
    void shouldRemoveReactionWhenReactionIsNone() {
        CommentReaction existingReaction = createLikeReaction();
        when(reactionRepository.findByCommentAndUser(comment, user)).thenReturn(Optional.of(existingReaction));

        reactionService.addCommentInfoReaction(comment, ReactionType.NONE);

        verify(reactionRepository).delete(existingReaction);
        assertThat(comment.getLikes()).isEqualTo(9);
        assertThat(comment.getDislikes()).isEqualTo(5);
    }

    @Test
    void shouldNotChangeCountsWhenReactionIsSame() {
        CommentReaction existingReaction = createLikeReaction();
        when(reactionRepository.findByCommentAndUser(comment, user)).thenReturn(Optional.of(existingReaction));

        reactionService.addCommentInfoReaction(comment, ReactionType.LIKE);

        verify(reactionRepository, never()).delete(existingReaction);
        verify(reactionRepository).save(existingReaction);
        assertThat(comment.getLikes()).isEqualTo(10);
        assertThat(comment.getDislikes()).isEqualTo(5);
    }

    private static CommentReaction createLikeReaction() {
        return CommentReaction.builder()
                              .user(user)
                              .comment(comment)
                              .reactionType(ReactionType.LIKE)
                              .build();
    }

}