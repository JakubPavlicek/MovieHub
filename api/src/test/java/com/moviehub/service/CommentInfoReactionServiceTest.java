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

import java.util.List;
import java.util.Optional;

import static com.moviehub.EntityBuilder.createCommentReaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentInfoReactionServiceTest {

    @Mock
    private CommentReactionRepository reactionRepository;

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
    }

    @Test
    void shouldAddNewLikeReaction() {
        when(reactionRepository.findByCommentInfoIdAndUserId(any(), any())).thenReturn(Optional.empty());

        reactionService.addCommentInfoReaction(comment, ReactionType.LIKE, user);

        verify(reactionRepository).save(any(CommentReaction.class));
        assertThat(comment.getLikes()).isEqualTo(11);
        assertThat(comment.getDislikes()).isEqualTo(5);
    }

    @Test
    void shouldUpdateExistingReactionToDislike() {
        CommentReaction existingReaction = createCommentReaction(comment, user, ReactionType.LIKE);

        when(reactionRepository.findByCommentInfoIdAndUserId(any(), any())).thenReturn(Optional.of(existingReaction));

        reactionService.addCommentInfoReaction(comment, ReactionType.DISLIKE, user);

        verify(reactionRepository).save(existingReaction);
        assertThat(existingReaction.getReactionType()).isEqualTo(ReactionType.DISLIKE);
        assertThat(comment.getLikes()).isEqualTo(9);
        assertThat(comment.getDislikes()).isEqualTo(6);
    }

    @Test
    void shouldRemoveReactionWhenReactionIsNone() {
        CommentReaction existingReaction = createCommentReaction(comment, user, ReactionType.LIKE);

        when(reactionRepository.findByCommentInfoIdAndUserId(any(), any())).thenReturn(Optional.of(existingReaction));

        reactionService.addCommentInfoReaction(comment, ReactionType.NONE, user);

        verify(reactionRepository).delete(existingReaction);
        assertThat(comment.getLikes()).isEqualTo(9);
        assertThat(comment.getDislikes()).isEqualTo(5);
    }

    @Test
    void shouldNotChangeCountsWhenReactionIsSame() {
        CommentReaction existingReaction = createCommentReaction(comment, user, ReactionType.LIKE);

        when(reactionRepository.findByCommentInfoIdAndUserId(any(), any())).thenReturn(Optional.of(existingReaction));

        reactionService.addCommentInfoReaction(comment, ReactionType.LIKE, user);

        verify(reactionRepository, never()).delete(existingReaction);
        verify(reactionRepository).save(existingReaction);
        assertThat(comment.getLikes()).isEqualTo(10);
        assertThat(comment.getDislikes()).isEqualTo(5);
    }

    @Test
    void shouldGetUserReactionsForComments() {
        CommentReaction commentReaction = createCommentReaction(comment, user, ReactionType.LIKE);

        when(reactionRepository.findUserReactionsByCommentIds(any(), any())).thenReturn(List.of(commentReaction));

        List<CommentReaction> userReactionsForComments = reactionService.getUserReactionsForComments(any(), any());

        assertThat(userReactionsForComments).hasSize(1);
        assertThat(userReactionsForComments.getFirst()).isEqualTo(commentReaction);
    }

}