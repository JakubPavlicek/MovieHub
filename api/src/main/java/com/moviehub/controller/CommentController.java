package com.moviehub.controller;

import com.moviehub.CommentsApi;
import com.moviehub.dto.AddCommentRequest;
import com.moviehub.dto.CommentInfoPage;
import com.moviehub.dto.ReactionTypeRequest;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.ReactionType;
import com.moviehub.mapper.dto.CommentInfoMapper;
import com.moviehub.mapper.dto.ReactionTypeMapper;
import com.moviehub.service.CommentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentsApi {

    private final CommentInfoService commentInfoService;

    @Override
    public ResponseEntity<Void> deleteComment(UUID commentId) {
        commentInfoService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> addReply(UUID commentId, AddCommentRequest addCommentRequest) {
        commentInfoService.addReply(commentId, addCommentRequest.getText());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<CommentInfoPage> getReplies(UUID commentId, Integer page, Integer limit) {
        Page<CommentReply> replies = commentInfoService.getReplies(commentId, page, limit);
        CommentInfoPage commentInfoPage = CommentInfoMapper.mapToCommentInfoPage(replies);

        return ResponseEntity.ok(commentInfoPage);
    }

    @Override
    public ResponseEntity<Void> addCommentReaction(UUID commentId, ReactionTypeRequest reactionTypeRequest) {
        ReactionType reactionType = ReactionTypeMapper.mapToReactionType(reactionTypeRequest);
        commentInfoService.addCommentReaction(commentId, reactionType);

        return ResponseEntity.noContent().build();
    }

}
