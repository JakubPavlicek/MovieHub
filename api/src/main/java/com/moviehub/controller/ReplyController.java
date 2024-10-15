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

@RestController
@RequiredArgsConstructor
public class ReplyController implements RepliesApi {

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
