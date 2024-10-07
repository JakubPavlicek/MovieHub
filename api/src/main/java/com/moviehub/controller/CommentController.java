package com.moviehub.controller;

import com.moviehub.CommentsApi;
import com.moviehub.dto.CommentInteractions;
import com.moviehub.dto.CommentList;
import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.dto.UserCommentReaction;
import com.moviehub.entity.ReactionType;
import com.moviehub.mapper.dto.CommentMapper;
import com.moviehub.mapper.dto.ReactionTypeMapper;
import com.moviehub.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentsApi {

    private final CommentService commentService;

    @Override
    public ResponseEntity<Void> deleteComment(UUID commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> addCommentReaction(UUID commentId, ReactionTypeRequest reactionTypeRequest) {
        ReactionType reactionType = ReactionTypeMapper.mapToReactionType(reactionTypeRequest);
        commentService.addCommentReaction(commentId, reactionType);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CommentInteractions> getUserCommentInteractions(CommentList commentList) {
        List<UserCommentReaction> reactions = commentService.getUserReactions(commentList.getComments());
        List<UUID> comments = commentService.getUserComments(commentList.getComments());

        CommentInteractions commentInteractions = CommentMapper.mapToCommentInteractions(reactions, comments);

        return ResponseEntity.ok(commentInteractions);
    }

}
