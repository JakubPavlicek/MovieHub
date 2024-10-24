package com.moviehub.mapper.entity;

import com.moviehub.entity.CommentInfo_;
import com.moviehub.entity.Comment_;
import lombok.extern.log4j.Log4j2;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for mapping comment fields to their corresponding database entity fields.
@Log4j2
public class CommentFieldMapper {

    // Private constructor to prevent instantiation.
    private CommentFieldMapper() {
    }

    /// Enumeration of the fields related to comments.
    public enum Field {
        COMMENT_ID,
        MOVIE,
        USER,
        CREATED_AT,
        TEXT,
        IS_DELETED,
        REPLIES,
        REACTIONS,
        DISLIKES,
        LIKES,
    }

    /// Maps a given comment field string to the corresponding entity field.
    ///
    /// @param field The name of the comment field to map.
    /// @return The corresponding entity field name as a string.
    public static String mapToCommentField(String field) {
        log.debug("mapping comment field: {}", field);
        // replaceAll: insert "_" before every uppercased letter
        CommentFieldMapper.Field commentField = CommentFieldMapper.Field.valueOf(field.replaceAll("([A-Z])", "_$1").toUpperCase());

        return switch (commentField) {
            case COMMENT_ID -> CommentInfo_.ID;
            case MOVIE -> Comment_.MOVIE;
            case USER -> CommentInfo_.USER;
            case CREATED_AT -> CommentInfo_.CREATED_AT;
            case TEXT -> CommentInfo_.TEXT;
            case IS_DELETED -> CommentInfo_.IS_DELETED;
            case REPLIES -> Comment_.REPLIES;
            case REACTIONS -> CommentInfo_.REACTIONS;
            case DISLIKES -> CommentInfo_.DISLIKES;
            case LIKES -> CommentInfo_.LIKES;
        };
    }

}
