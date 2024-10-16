package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentInfo_;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
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
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createComment;
import static com.moviehub.EntityBuilder.createReply;
import static com.moviehub.EntityBuilder.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentInfoServiceTest {

    @Mock
    private CommentService commentService;

    @Mock
    private CommentReplyService replyService;

    @Mock
    private CommentInfoReactionService reactionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentInfoService commentInfoService;

    private static final UUID COMMENT_ID = UUID.randomUUID();
    private static final UUID REPLY_ID = UUID.randomUUID();
    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final String COMMENT_TEXT = "This is a comment.";
    private static final String REPLY_TEXT = "This is a reply.";
    private static final ReactionType REACTION_TYPE = ReactionType.LIKE;
    private static final Sort SORT = Sort.by(CommentInfo_.CREATED_AT).ascending();
    private static final Pageable PAGEABLE = PageRequest.of(0, 10, SORT);

    private static User user;

    @BeforeEach
    void setUp() {
        user = createUser("John");
    }

    @Test
    void shouldDeleteComment() {
        when(userService.getUser()).thenReturn(user);
        doNothing().when(commentService)
                   .deleteComment(COMMENT_ID, user);

        commentInfoService.deleteComment(COMMENT_ID);

        verify(commentService).deleteComment(COMMENT_ID, user);
    }

    @Test
    void shouldDeleteReply() {
        when(userService.getUser()).thenReturn(user);
        doNothing().when(replyService)
                   .deleteReply(REPLY_ID, user);

        commentInfoService.deleteReply(REPLY_ID);

        verify(replyService).deleteReply(REPLY_ID, user);
    }

    @Test
    void shouldAddCommentReaction() {
        Comment comment = (Comment) createComment(null, user, COMMENT_TEXT);

        when(userService.getUser()).thenReturn(user);
        when(commentService.getComment(COMMENT_ID)).thenReturn(comment);
        doNothing().when(reactionService)
                   .addCommentInfoReaction(comment, REACTION_TYPE, user);

        commentInfoService.addCommentReaction(COMMENT_ID, REACTION_TYPE);

        verify(reactionService).addCommentInfoReaction(comment, REACTION_TYPE, user);
    }

    @Test
    void shouldAddReplyReaction() {
        CommentReply reply = (CommentReply) createReply(null, user, REPLY_TEXT);

        when(userService.getUser()).thenReturn(user);
        when(replyService.getReply(REPLY_ID)).thenReturn(reply);
        doNothing().when(reactionService)
                   .addCommentInfoReaction(reply, REACTION_TYPE, user);

        commentInfoService.addReplyReaction(REPLY_ID, REACTION_TYPE);

        verify(reactionService).addCommentInfoReaction(reply, REACTION_TYPE, user);
    }

    @Test
    void shouldAddComment() {
        Movie movie = new Movie();

        when(userService.getUser()).thenReturn(user);

        commentInfoService.addComment(movie, COMMENT_TEXT);

        verify(commentService).addComment(movie, COMMENT_TEXT, user);
    }

    @Test
    void shouldAddReply() {
        Comment comment = (Comment) createComment(null, user, COMMENT_TEXT);

        when(userService.getUser()).thenReturn(user);
        when(commentService.getComment(COMMENT_ID)).thenReturn(comment);

        commentInfoService.addReply(COMMENT_ID, REPLY_TEXT);

        verify(replyService).addReply(comment, REPLY_TEXT, user);
    }

    @Test
    void shouldGetComments() {
        Comment comment = (Comment) createComment(null, user, COMMENT_TEXT);
        Page<Comment> commentsPage = new PageImpl<>(List.of(comment), PAGEABLE, 1);

        when(userService.getUser()).thenReturn(user);
        when(commentService.getComments(MOVIE_ID, PAGEABLE)).thenReturn(commentsPage);

        Page<Comment> resultPage = commentInfoService.getComments(MOVIE_ID, PAGEABLE);

        assertThat(resultPage.getTotalElements()).isEqualTo(1);
        assertThat(resultPage.getContent()).containsExactly(comment);
    }

    @Test
    void shouldGetReplies() {
        CommentReply reply = (CommentReply) createReply(null, user, REPLY_TEXT);
        Page<CommentReply> repliesPage = new PageImpl<>(List.of(reply));

        when(userService.getUser()).thenReturn(user);
        when(replyService.getReplies(COMMENT_ID, PAGEABLE)).thenReturn(repliesPage);

        Page<CommentReply> resultPage = commentInfoService.getReplies(COMMENT_ID, 0, 10);

        assertThat(resultPage.getTotalElements()).isEqualTo(1);
        assertThat(resultPage.getContent()).containsExactly(reply);
    }

}