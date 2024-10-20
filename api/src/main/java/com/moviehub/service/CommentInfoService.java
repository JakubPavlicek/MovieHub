package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentInfo;
import com.moviehub.entity.CommentInfo_;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing comments and replies.
/// This class provides methods for adding, deleting, and retrieving comments and replies,
/// as well as managing user reactions to them.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CommentInfoService {

    /// Service for managing Comment entities.
    private final CommentService commentService;

    /// Service for managing CommentReply entities.
    private final CommentReplyService replyService;

    /// Service for managing reactions to comments.
    private final CommentInfoReactionService reactionService;

    /// Service for managing User entities.
    private final UserService userService;

    /// Deletes a comment by its ID.
    /// The operation is performed by the currently authenticated user.
    ///
    /// @param commentId The ID of the comment to delete.
    public void deleteComment(UUID commentId) {
        log.info("deleting comment: {}", commentId);

        User user = userService.getUser();

        commentService.deleteComment(commentId, user);
    }

    /// Deletes a reply by its ID.
    /// The operation is performed by the currently authenticated user.
    ///
    /// @param replyId The ID of the reply to delete.
    public void deleteReply(UUID replyId) {
        log.info("deleting reply: {}", replyId);

        User user = userService.getUser();

        replyService.deleteReply(replyId, user);
    }

    /// Adds a reaction to a specific comment by the currently authenticated user.
    ///
    /// @param commentId The ID of the comment to react to.
    /// @param reactionType The type of reaction to add (like, dislike, etc.).
    public void addCommentReaction(UUID commentId, ReactionType reactionType) {
        Comment comment = commentService.getComment(commentId);
        User user = userService.getUser();

        reactionService.addCommentInfoReaction(comment, reactionType, user);
    }

    /// Adds a reaction to a specific reply by the currently authenticated user.
    ///
    /// @param replyId The ID of the reply to react to.
    /// @param reactionType The type of reaction to add (like, dislike, etc.).
    public void addReplyReaction(UUID replyId, ReactionType reactionType) {
        CommentReply reply = replyService.getReply(replyId);
        User user = userService.getUser();

        reactionService.addCommentInfoReaction(reply, reactionType, user);
    }

    /// Adds a comment to a specific movie by the currently authenticated user.
    ///
    /// @param movie The movie to which the comment is added.
    /// @param text The text of the comment.
    public void addComment(Movie movie, String text) {
        User user = userService.getUser();

        commentService.addComment(movie, text, user);
    }

    /// Adds a reply to a specific comment by the currently authenticated user.
    ///
    /// @param commentId The ID of the comment to reply to.
    /// @param text The text of the reply.
    public void addReply(UUID commentId, String text) {
        Comment comment = commentService.getComment(commentId);
        User user = userService.getUser();

        replyService.addReply(comment, text, user);
    }

    /// Retrieves a paginated list of comments for a specific movie.
    /// Sets the user reactions for the retrieved comments.
    ///
    /// @param movieId The ID of the movie for which to retrieve comments.
    /// @param pageable The pagination information.
    /// @return A paginated list of comments.
    public Page<Comment> getComments(UUID movieId, Pageable pageable) {
        Page<Comment> comments = commentService.getComments(movieId, pageable);

        return setUserReactions(comments);
    }

    /// Retrieves a paginated list of replies for a specific comment.
    /// Sets the user reactions for the retrieved replies.
    ///
    /// @param commentId The ID of the comment for which to retrieve replies.
    /// @param page The page number.
    /// @param limit The number of replies per page.
    /// @return A paginated list of replies.
    public Page<CommentReply> getReplies(UUID commentId, Integer page, Integer limit) {
        Sort sort = Sort.by(CommentInfo_.CREATED_AT).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<CommentReply> replies = replyService.getReplies(commentId, pageable);

        return setUserReactions(replies);
    }

    /// Sets the user reactions for a paginated list of comment information.
    /// This includes fetching the user's reactions and updating the transient fields.
    ///
    /// @param commentInfoPage The paginated list of CommentInfo objects.
    /// @param <T> The type of CommentInfo (Comment or CommentReply).
    /// @return The updated paginated list of CommentInfo objects with user reactions.
    private <T extends CommentInfo> Page<T> setUserReactions(Page<T> commentInfoPage) {
        User user = userService.getUser();

        if (user == null) {
            return commentInfoPage;
        }

        // fetch reactions for comments/replies in one query
        List<UUID> commentInfoIds = commentInfoPage.stream()
                                                   .map(CommentInfo::getId)
                                                   .toList();
        List<CommentReaction> reactions = reactionService.getUserReactionsForComments(user.getId(), commentInfoIds);

        // set transient fields
        commentInfoPage.forEach(commentInfo -> {
            commentInfo.setUserReaction(reactions);
            commentInfo.setIsAuthor(user);
        });

        return commentInfoPage;
    }

}
