package com.moviehub.mapper.dto;

import com.moviehub.dto.CommentInfoDetailsResponse;
import com.moviehub.dto.CommentInfoPage;
import com.moviehub.entity.CommentInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public class CommentInfoMapper {

    private CommentInfoMapper() {
    }

    public static CommentInfoDetailsResponse mapToCommentInfoDetailsResponse(CommentInfo commentInfo) {
        return CommentInfoDetailsResponse.builder()
                                     .id(commentInfo.getId())
                                     .author(UserMapper.toUserNameAndPictureUrl(commentInfo.getUser()))
                                     .createdAt(commentInfo.getCreatedAt())
                                     .text(commentInfo.getText())
                                     .isDeleted(commentInfo.getIsDeleted())
                                     .likes(commentInfo.getLikes())
                                     .dislikes(commentInfo.getDislikes())
                                     .userReaction(ReactionTypeMapper.mapToReactionType(commentInfo.getUserReaction()))
                                     .isAuthor(commentInfo.isAuthor())
                                     .build();
    }

    public static CommentInfoPage mapToCommentInfoPage(Page<? extends CommentInfo> commentInfos) {
        return CommentInfoPage.builder()
                          .content(mapToCommentInfoDetailsResponse(commentInfos))
                          .pageable(PageableMapper.mapToPageableDTO(commentInfos.getPageable()))
                          .last(commentInfos.isLast())
                          .totalElements(commentInfos.getTotalElements())
                          .totalPages(commentInfos.getTotalPages())
                          .first(commentInfos.isFirst())
                          .size(commentInfos.getSize())
                          .number(commentInfos.getNumber())
                          .sort(SortMapper.mapToSortDTO(commentInfos.getSort()))
                          .numberOfElements(commentInfos.getNumberOfElements())
                          .empty(commentInfos.isEmpty())
                          .build();
    }

    private static List<CommentInfoDetailsResponse> mapToCommentInfoDetailsResponse(Page<? extends CommentInfo> commentInfos) {
        return commentInfos.stream()
                       .map(CommentInfoMapper::mapToCommentInfoDetailsResponse)
                       .toList();
    }

}
