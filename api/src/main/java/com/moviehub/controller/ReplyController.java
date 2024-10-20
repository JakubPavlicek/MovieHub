package com.moviehub.controller;

import com.moviehub.RepliesApi;
import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.entity.ReactionType;
import com.moviehub.mapper.dto.ReactionTypeMapper;
import com.moviehub.service.CommentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Controller class for managing replies to comments in the movie hub application.
/// It provides endpoints for performing operations related to comment replies.
@RestController
@RequiredArgsConstructor
public class ReplyController implements RepliesApi {

    /// Service for handling operations related to comment information and replies.
    private final CommentInfoService commentInfoService;

    @Override
    public ResponseEntity<Void> addReplyReaction(UUID replyId, ReactionTypeRequest reactionTypeRequest) {
        ReactionType reactionType = ReactionTypeMapper.mapToReactionType(reactionTypeRequest);
        commentInfoService.addReplyReaction(replyId, reactionType);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteReply(UUID replyId) {
        commentInfoService.deleteReply(replyId);

        return ResponseEntity.noContent().build();
    }

}
