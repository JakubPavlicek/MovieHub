package com.moviehub.mapper.dto;

import com.moviehub.dto.AddCommentRequest;
import com.moviehub.dto.CommentDetailsResponse;
import com.moviehub.dto.CommentPage;
import com.moviehub.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public class CommentMapper {

    private CommentMapper() {
    }

    public static Comment mapToComment(AddCommentRequest addCommentRequest) {
        return Comment.builder()
                      .text(addCommentRequest.getText())
                      .parentCommentId(addCommentRequest.getParentCommentId())
                      .build();
    }

    public static CommentDetailsResponse mapToCommentDetailsResponse(Comment comment) {
        return CommentDetailsResponse.builder()
                                     .id(comment.getId())
                                     .movieId(comment.getMovie().getId())
                                     .userId(comment.getUserId())
                                     .createdAt(comment.getCreatedAt())
                                     .text(comment.getText())
                                     .build();
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

}