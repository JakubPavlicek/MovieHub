package com.moviehub.controller;

import com.moviehub.CommentsApi;
import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentsApi {

    private final CommentService commentService;

    @Override
    public ResponseEntity<Void> deleteComment(String commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> addCommentReaction(String commentId, ReactionTypeRequest reactionTypeRequest) {
        commentService.addCommentReaction(commentId, reactionTypeRequest.getReactionType().getValue());

        return ResponseEntity.noContent().build();
    }

}
