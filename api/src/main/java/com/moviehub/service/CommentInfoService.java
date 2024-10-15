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

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CommentInfoService {

    private final CommentService commentService;
    private final CommentReplyService replyService;
    private final CommentInfoReactionService reactionService;
    private final UserService userService;

    public void deleteComment(UUID commentId) {
        User user = userService.getUser();

        commentService.deleteComment(commentId, user);
    }

    public void deleteReply(UUID replyId) {
        User user = userService.getUser();

        replyService.deleteReply(replyId, user);
    }

    public void addCommentReaction(UUID commentId, ReactionType reactionType) {
        Comment comment = commentService.getComment(commentId);
        User user = userService.getUser();

        reactionService.addCommentInfoReaction(comment, reactionType, user);
    }

    public void addReplyReaction(UUID replyId, ReactionType reactionType) {
        CommentReply reply = replyService.getReply(replyId);
        User user = userService.getUser();

        reactionService.addCommentInfoReaction(reply, reactionType, user);
    }

    public void addComment(Movie movie, String text) {
        User user = userService.getUser();

        commentService.addComment(movie, text, user);
    }

    public void addReply(UUID commentId, String text) {
        Comment comment = commentService.getComment(commentId);
        User user = userService.getUser();

        replyService.addReply(comment, text, user);
    }

    public Page<Comment> getComments(UUID movieId, Pageable pageable) {
        Page<Comment> comments = commentService.getComments(movieId, pageable);

        return setUserReactions(comments);
    }

    public Page<CommentReply> getReplies(UUID commentId, Integer page, Integer limit) {
        Sort sort = Sort.by(CommentInfo_.CREATED_AT).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<CommentReply> replies = replyService.getReplies(commentId, pageable);

        return setUserReactions(replies);
    }

    private <T extends CommentInfo> Page<T> setUserReactions(Page<T> commentInfoPage) {
        User user = userService.getUser();

        // fetch reactions for comments/replies in one query
        List<UUID> commentInfoIds = commentInfoPage.stream().map(CommentInfo::getId).toList();
        List<CommentReaction> reactions = reactionService.getUserReactionsForComments(user.getId(), commentInfoIds);

        // set transient fields
        commentInfoPage.forEach(commentInfo -> {
            commentInfo.setUserReaction(reactions);
            commentInfo.setIsAuthor(user);
        });

        return commentInfoPage;
    }

}
