package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.User;
import com.moviehub.exception.ReplyNotFoundException;
import com.moviehub.repository.CommentReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReplyService {

    private final CommentReplyRepository replyRepository;

    private static final String REPLY_DELETED = "Reply deleted.";

    public CommentReply getReply(UUID replyId) {
        return replyRepository.findById(replyId)
                              .orElseThrow(() -> new ReplyNotFoundException("Reply with ID: " + replyId + " not found"));
    }

    public CommentReply getReplyByUser(UUID replyId, User user) {
        return replyRepository.findByIdAndUser(replyId, user)
                              .orElseThrow(() -> new ReplyNotFoundException("Reply with ID: " + replyId + " not found"));
    }

    public void deleteReply(UUID replyId, User user) {
        CommentReply commentReply = getReplyByUser(replyId, user);

        commentReply.setText(REPLY_DELETED);
        commentReply.setIsDeleted(true);

        replyRepository.save(commentReply);
    }

    public void addReply(Comment comment, String text, User user) {
        CommentReply commentReply = new CommentReply();

        commentReply.setComment(comment);
        commentReply.setText(text);
        commentReply.setUser(user);

        replyRepository.save(commentReply);
    }

    public Page<CommentReply> getReplies(UUID commentId, Pageable pageable) {
        return replyRepository.findAllReplies(commentId, pageable);
    }

}
