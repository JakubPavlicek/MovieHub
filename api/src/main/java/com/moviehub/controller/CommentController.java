package com.moviehub.controller;

import com.moviehub.CommentsApi;
import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.entity.ReactionType;
import com.moviehub.mapper.dto.ReactionTypeMapper;
import com.moviehub.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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

}
