package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReply;
import com.moviehub.exception.ReplyNotFoundException;
import com.moviehub.repository.CommentReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentReplyService {

    private final CommentReplyRepository replyRepository;

    private static final String REPLY_DELETED = "Reply deleted.";

    public void deleteReply(UUID replyId) {
        CommentReply commentReply = getReply(replyId);

        commentReply.setText(REPLY_DELETED);
        commentReply.setIsDeleted(true);

        replyRepository.save(commentReply);
    }

    public CommentReply getReply(UUID replyId) {
        return replyRepository.findById(replyId)
                              .orElseThrow(() -> new ReplyNotFoundException("Reply with ID: " + replyId + " not found"));
    }

    public void addReply(Comment comment, String text) {
        CommentReply commentReply = new CommentReply();

        commentReply.setComment(comment);
        commentReply.setText(text);

        replyRepository.save(commentReply);
    }

}
