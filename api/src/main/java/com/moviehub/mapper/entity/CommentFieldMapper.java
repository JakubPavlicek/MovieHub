package com.moviehub.mapper.entity;

import com.moviehub.entity.Comment_;

public class CommentFieldMapper {

    private CommentFieldMapper() {
    }

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

    public static String mapToCommentField(String field) {
        // replaceAll: insert "_" before every uppercased letter
        CommentFieldMapper.Field commentField = CommentFieldMapper.Field.valueOf(field.replaceAll("([A-Z])", "_$1").toUpperCase());

        return switch (commentField) {
            case COMMENT_ID -> Comment_.ID;
            case MOVIE -> Comment_.MOVIE;
            case USER -> Comment_.USER;
            case CREATED_AT -> Comment_.CREATED_AT;
            case TEXT -> Comment_.TEXT;
            case IS_DELETED -> Comment_.IS_DELETED;
            case REPLIES -> Comment_.REPLIES;
            case REACTIONS -> Comment_.REACTIONS;
            case DISLIKES -> Comment_.DISLIKES;
            case LIKES -> Comment_.LIKES;
        };
    }

}
