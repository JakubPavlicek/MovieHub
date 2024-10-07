package com.moviehub.mapper.dto;

import com.moviehub.dto.AddCommentRequest;
import com.moviehub.dto.CommentDetailsResponse;
import com.moviehub.dto.CommentInteractions;
import com.moviehub.dto.CommentPage;
import com.moviehub.dto.CommentReaction;
import com.moviehub.dto.UserCommentReaction;
import com.moviehub.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public class CommentMapper {

    private CommentMapper() {
    }

    public static Comment mapToComment(AddCommentRequest addCommentRequest) {
        return Comment.builder()
                      .text(addCommentRequest.getText())
                      .build();
    }

    public static CommentDetailsResponse mapToCommentDetailsResponse(Comment comment) {
        return CommentDetailsResponse.builder()
                                     .id(comment.getId())
                                     .movieId(comment.getMovie().getId())
                                     .user(UserMapper.toUserNameAndPictureUrl(comment.getUser()))
                                     .createdAt(comment.getCreatedAt())
                                     .text(comment.getText())
                                     .isDeleted(comment.getIsDeleted())
                                     .likes(comment.getLikes())
                                     .dislikes(comment.getDislikes())
                                     .replies(mapRepliesToCommentDetailsResponse(comment.getReplies()))
                                     .build();
    }

    private static CommentDetailsResponse mapReplyToCommentDetailsResponse(Comment comment) {
        return CommentDetailsResponse.builder()
                                     .id(comment.getId())
                                     .movieId(comment.getMovie().getId())
                                     .user(UserMapper.toUserNameAndPictureUrl(comment.getUser()))
                                     .createdAt(comment.getCreatedAt())
                                     .text(comment.getText())
                                     .isDeleted(comment.getIsDeleted())
                                     .likes(comment.getLikes())
                                     .dislikes(comment.getDislikes())
                                     .build();
    }

    private static List<CommentDetailsResponse> mapRepliesToCommentDetailsResponse(List<Comment> replies) {
        return replies.stream()
                      .map(CommentMapper::mapReplyToCommentDetailsResponse)
                      .toList();
    }

    public static CommentPage mapToCommentPage(Page<Comment> comments) {
        return CommentPage.builder()
                          .content(mapToCommentDetailsResponse(comments))
                          .pageable(PageableMapper.mapToPageableDTO(comments.getPageable()))
                          .last(comments.isLast())
                          .totalElements(comments.getTotalElements())
                          .totalPages(comments.getTotalPages())
                          .first(comments.isFirst())
                          .size(comments.getSize())
                          .number(comments.getNumber())
                          .sort(SortMapper.mapToSortDTO(comments.getSort()))
                          .numberOfElements(comments.getNumberOfElements())
                          .empty(comments.isEmpty())
                          .build();
    }

    private static List<CommentDetailsResponse> mapToCommentDetailsResponse(Page<Comment> comments) {
        return comments.stream()
                       .map(CommentMapper::mapToCommentDetailsResponse)
                       .toList();
    }

    public static CommentInteractions mapToCommentInteractions(List<UserCommentReaction> reactions, List<UUID> comments) {
        return CommentInteractions.builder()
                                  .comments(comments)
                                  .reactions(mapToCommentReactions(reactions))
                                  .build();
    }

    private static List<CommentReaction> mapToCommentReactions(List<UserCommentReaction> reactions) {
        return reactions.stream()
                        .map(CommentMapper::mapToCommentReaction)
                        .toList();
    }

    private static CommentReaction mapToCommentReaction(UserCommentReaction userCommentReaction) {
        return CommentReaction.builder()
                              .commentId(userCommentReaction.commentId())
                              .reaction(ReactionTypeMapper.mapToReactionType(userCommentReaction.reactionType()))
                              .build();
    }

}
