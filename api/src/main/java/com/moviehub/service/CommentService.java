package com.moviehub.service;

import com.moviehub.dto.UserCommentReaction;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ReactionType;
import com.moviehub.exception.CommentNotFoundException;
import com.moviehub.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentReactionService reactionService;

    private final UserService userService;

    private static final String DELETION_TEXT = "Comment deleted.";

    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new CommentNotFoundException("Comment with ID: " + commentId + " not found"));
    }

    public Comment saveComment(Movie movie, Comment comment, UUID parentCommentId) {
        ensureValidParentCommentId(parentCommentId);

        comment.setMovie(movie);
        comment.setUser(userService.getUser());
        comment.setParentComment(getComment(parentCommentId));

        return commentRepository.save(comment);
    }

    private void ensureValidParentCommentId(UUID parentCommentId) {
        if (parentCommentId == null) {
            return;
        }

        if (!commentRepository.existsById(parentCommentId)) {
            throw new CommentNotFoundException("Parent comment with ID: " + parentCommentId + " not found");
        }
    }

    public Page<Comment> getComments(Movie movie, Pageable pageable) {
        Page<Comment> topLevelComments = commentRepository.findAllTopLevelComments(movie, pageable);

        // extract the IDs of the top-level comments to fetch their replies
        List<UUID> commentIds = topLevelComments.getContent()
                                                .stream()
                                                .map(Comment::getId)
                                                .toList();

        // fetch replies in one query for all top-level comments
        List<Comment> replies = commentRepository.findRepliesForComments(commentIds, pageable.getSort());

        // map the replies to their corresponding top-level comments
        Map<UUID, List<Comment>> repliesGroupedByCommentId = replies.stream()
                                                                    .collect(Collectors.groupingBy(reply -> reply.getParentComment().getId()));

        // add replies to the corresponding top-level comments
        topLevelComments.forEach(comment -> {
            List<Comment> commentReplies = repliesGroupedByCommentId.getOrDefault(comment.getId(), Collections.emptyList());
            comment.setReplies(commentReplies);
        });

        return topLevelComments;
    }

    public void deleteComment(UUID commentId) {
        Comment comment = getComment(commentId);

        comment.setText(DELETION_TEXT);
        comment.setIsDeleted(true);

        commentRepository.save(comment);
    }

    public void addCommentReaction(UUID commentId, ReactionType reactionType) {
        Comment comment = getComment(commentId);
        reactionService.addCommentReaction(comment, reactionType);
    }

    public List<UserCommentReaction> getUserReactions(List<UUID> commentIds) {
        return reactionService.getUserReactions(commentIds);
    }

    public List<UUID> getUserComments(List<UUID> commentIds) {
        return commentRepository.filterCommentsPostedByUser(commentIds, userService.getUser());
    }

}
