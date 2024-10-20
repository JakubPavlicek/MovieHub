package com.moviehub.mapper.dto;

import com.moviehub.dto.CommentInfoDetailsResponse;
import com.moviehub.dto.CommentInfoPage;
import com.moviehub.entity.CommentInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between CommentInfo entities and Data Transfer Objects (DTOs).
public class CommentInfoMapper {

    // Private constructor to prevent instantiation.
    private CommentInfoMapper() {
    }

    /// Maps a CommentInfo entity to a CommentInfoDetailsResponse DTO.
    ///
    /// @param commentInfo The CommentInfo entity to map.
    /// @return A CommentInfoDetailsResponse DTO containing the comment information.
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

    /// Maps a Page of CommentInfo entities to a CommentInfoPage DTO.
    ///
    /// @param commentInfos The page of CommentInfo entities to map.
    /// @return A CommentInfoPage DTO containing the mapped comment information and pagination info.
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

    /// Maps a Page of CommentInfo entities to a list of CommentInfoDetailsResponse DTOs.
    ///
    /// @param commentInfos The page of CommentInfo entities to map.
    /// @return A list of CommentInfoDetailsResponse DTOs.
    private static List<CommentInfoDetailsResponse> mapToCommentInfoDetailsResponse(Page<? extends CommentInfo> commentInfos) {
        return commentInfos.stream()
                           .map(CommentInfoMapper::mapToCommentInfoDetailsResponse)
                           .toList();
    }

}
