package com.moviehub.service;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentInfo;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentInfoService {

    private final CommentService commentService;
    private final CommentReplyService replyService;
    private final CommentInfoReactionService reactionService;
    private final ParseService parseService;
    private final UserService userService;

    public void deleteComment(UUID commentId) {
        commentService.deleteComment(commentId);
    }

    public void deleteReply(UUID replyId) {
        replyService.deleteReply(replyId);
    }

    public void addCommentReaction(UUID commentId, ReactionType reactionType) {
        Comment comment = commentService.getComment(commentId);

        reactionService.addCommentInfoReaction(comment, reactionType);
    }

    public void addReplyReaction(UUID replyId, ReactionType reactionType) {
        CommentReply reply = replyService.getReply(replyId);

        reactionService.addCommentInfoReaction(reply, reactionType);
    }

    public void addComment(Movie movie, String text) {
        commentService.addComment(movie, text);
    }

    public void addReply(UUID commentId, String text) {
        Comment comment = commentService.getComment(commentId);

        replyService.addReply(comment, text);
    }

    public Page<Comment> getComments(UUID movieId, Pageable pageable) {
        Page<Comment> comments = commentService.getComments(movieId, pageable);

        User authUser = userService.getUser();

        // set transient fields for comments and their replies
        comments.forEach(comment -> {
            log.info("setting transient fields for comment with ID: {}", comment.getId());
            setTransientFields(comment, authUser);
            comment.getReplies().forEach(reply -> setTransientFields(reply, authUser));
        });

        return comments;
    }

    private void setTransientFields(CommentInfo commentInfo, User user) {
        if (user == null) {
            return;
        }

        commentInfo.setUserReaction(user);
        commentInfo.setAuthorFlag(user);
    }

    public Page<CommentReply> getReplies(UUID commentId, Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseCommentSort(sort));

        return commentService.getReplies(commentId, pageable);
    }

}
