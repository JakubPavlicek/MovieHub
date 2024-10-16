package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.User;
import com.moviehub.exception.ReplyNotFoundException;
import com.moviehub.repository.CommentReplyRepository;
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
import java.util.Optional;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createReply;
import static com.moviehub.EntityBuilder.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentReplyServiceTest {

    @Mock
    private CommentReplyRepository replyRepository;

    @InjectMocks
    private CommentReplyService commentReplyService;

    private static final UUID REPLY_ID = UUID.randomUUID();
    private static final UUID COMMENT_ID = UUID.randomUUID();
    private static final String FIRST_REPLY_TEXT = "Reply 1";
    private static final String DELETED_REPLY_TEXT = "Reply deleted.";
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    private static User user;
    private static Comment comment;
    private static CommentReply reply;

    @BeforeEach
    void setUp() {
        user = createUser("John");
        comment = new Comment();
        reply = (CommentReply) createReply(comment, user, FIRST_REPLY_TEXT);
    }

    @Test
    void shouldGetReplyWhenReplyExists() {
        when(replyRepository.findById(REPLY_ID)).thenReturn(Optional.of(reply));

        CommentReply foundReply = commentReplyService.getReply(REPLY_ID);

        assertThat(foundReply.getText()).isEqualTo(FIRST_REPLY_TEXT);
    }

    @Test
    void shouldThrowReplyNotFoundExceptionWhenReplyDoesNotExist() {
        when(replyRepository.findById(REPLY_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ReplyNotFoundException.class)
            .isThrownBy(() -> commentReplyService.getReply(REPLY_ID));
    }

    @Test
    void shouldGetReplyByUserWhenReplyExists() {
        when(replyRepository.findByIdAndUser(REPLY_ID, user)).thenReturn(Optional.of(reply));

        CommentReply foundReply = commentReplyService.getReplyByUser(REPLY_ID, user);

        assertThat(foundReply.getText()).isEqualTo(FIRST_REPLY_TEXT);
    }

    @Test
    void shouldThrowReplyNotFoundExceptionWhenReplyDoesNotExistForUser() {
        when(replyRepository.findByIdAndUser(REPLY_ID, user)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ReplyNotFoundException.class)
            .isThrownBy(() -> commentReplyService.getReplyByUser(REPLY_ID, user));
    }

    @Test
    void shouldAddReply() {
        when(replyRepository.save(any(CommentReply.class))).thenReturn(reply);

        commentReplyService.addReply(comment, FIRST_REPLY_TEXT, user);

        verify(replyRepository).save(any(CommentReply.class));
    }

    @Test
    void shouldDeleteReply() {
        when(replyRepository.findByIdAndUser(REPLY_ID, user)).thenReturn(Optional.of(reply));

        commentReplyService.deleteReply(REPLY_ID, user);

        assertThat(reply.getIsDeleted()).isTrue();
        assertThat(reply.getText()).isEqualTo(DELETED_REPLY_TEXT);

        verify(replyRepository).save(reply);
    }

    @Test
    void shouldThrowReplyNotFoundExceptionWhenDeletingNonExistentReply() {

        when(replyRepository.findByIdAndUser(REPLY_ID, user)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ReplyNotFoundException.class)
            .isThrownBy(() -> commentReplyService.deleteReply(REPLY_ID, user));
    }

    @Test
    void shouldGetReplies() {
        CommentReply reply2 = (CommentReply) createReply(comment, user, "Reply 2");
        Page<CommentReply> replyPage = new PageImpl<>(List.of(reply, reply2));

        when(replyRepository.findAllReplies(COMMENT_ID, PAGEABLE)).thenReturn(replyPage);

        Page<CommentReply> resultPage = commentReplyService.getReplies(COMMENT_ID, PAGEABLE);

        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent()).containsExactlyInAnyOrder(reply, reply2);
    }

}