package com.moviehub.mapper.entity;

import com.moviehub.entity.Comment_;

public class CommentFieldMapper {

    private CommentFieldMapper() {
    }

    public enum Field {
        COMMENT_ID,
        MOVIE_ID,
        USER_ID,
        CREATED_AT,
        TEXT,
        PARENT_COMMENT_ID,
        REACTIONS
    }

    public static String mapToCommentField(String field) {
        // replaceAll: insert "_" before every uppercased letter
        CommentFieldMapper.Field commentField = CommentFieldMapper.Field.valueOf(field.replaceAll("([A-Z])", "_$1").toUpperCase());

        return switch (commentField) {
            case COMMENT_ID -> Comment_.ID;
            case MOVIE_ID -> Comment_.MOVIE;
            case USER_ID -> Comment_.USER_ID;
            case CREATED_AT -> Comment_.CREATED_AT;
            case TEXT -> Comment_.TEXT;
            case PARENT_COMMENT_ID -> Comment_.PARENT_COMMENT_ID;
            case REACTIONS -> Comment_.REACTIONS;
        };
    }

}
