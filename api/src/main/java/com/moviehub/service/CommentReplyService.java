package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.User;
import com.moviehub.exception.ReplyNotFoundException;
import com.moviehub.repository.CommentReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing comment replies.
/// This class provides methods for adding, retrieving, and deleting replies to comments.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CommentReplyService {

    /// Repository for managing CommentReply entities.
    private final CommentReplyRepository replyRepository;

    /// Constant message indicating that a reply has been deleted.
    private static final String REPLY_DELETED = "Reply deleted.";

    /// Retrieves a reply by its ID.
    ///
    /// @param replyId The ID of the reply to retrieve.
    /// @return The CommentReply associated with the specified ID.
    /// @throws ReplyNotFoundException if the reply does not exist.
    public CommentReply getReply(UUID replyId) {
        return replyRepository.findById(replyId)
                              .orElseThrow(() -> new ReplyNotFoundException("Reply with ID: " + replyId + " not found"));
    }

    /// Retrieves a reply by its ID and the associated user.
    ///
    /// @param replyId The ID of the reply to retrieve.
    /// @param user The user associated with the reply.
    /// @return The CommentReply associated with the specified ID and user.
    /// @throws ReplyNotFoundException if the reply does not exist for the specified user.
    public CommentReply getReplyByUser(UUID replyId, User user) {
        return replyRepository.findByIdAndUser(replyId, user)
                              .orElseThrow(() -> new ReplyNotFoundException("Reply with ID: " + replyId + " not found"));
    }

    /// Retrieves a paginated list of replies for a specific comment.
    ///
    /// @param commentId The ID of the comment for which to retrieve replies.
    /// @param pageable The pagination information.
    /// @return A paginated list of CommentReply entities associated with the comment.
    public Page<CommentReply> getReplies(UUID commentId, Pageable pageable) {
        log.info("fetching replies for comment: {}", commentId);

        return replyRepository.findAllReplies(commentId, pageable);
    }

    /// Adds a reply to a specific comment by the user.
    ///
    /// @param comment The comment to which the reply is added.
    /// @param text The text of the reply.
    /// @param user The user adding the reply.
    public void addReply(Comment comment, String text, User user) {
        log.info("adding reply to comment: {}", comment.getId());

        CommentReply commentReply = new CommentReply();

        commentReply.setComment(comment);
        commentReply.setText(text);
        commentReply.setUser(user);

        replyRepository.save(commentReply);
    }

    /// Deletes a reply by its ID, marking it as deleted instead of removing it from the database.
    ///
    /// @param replyId The ID of the reply to delete.
    /// @param user The user attempting to delete the reply.
    public void deleteReply(UUID replyId, User user) {
        CommentReply commentReply = getReplyByUser(replyId, user);

        commentReply.setText(REPLY_DELETED);
        commentReply.setIsDeleted(true);

        replyRepository.save(commentReply);
    }

}
