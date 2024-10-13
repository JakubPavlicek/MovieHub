package com.moviehub.mapper.entity;

import com.moviehub.entity.Comment_;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CommentFieldMapperTest {

    private static Stream<Arguments> commentFieldNames() {
        return Stream.of(
            Arguments.of(CommentFieldMapper.Field.COMMENT_ID, Comment_.ID),
            Arguments.of(CommentFieldMapper.Field.MOVIE, Comment_.MOVIE),
            Arguments.of(CommentFieldMapper.Field.USER, Comment_.USER),
            Arguments.of(CommentFieldMapper.Field.CREATED_AT, Comment_.CREATED_AT),
            Arguments.of(CommentFieldMapper.Field.TEXT, Comment_.TEXT),
            Arguments.of(CommentFieldMapper.Field.IS_DELETED, Comment_.IS_DELETED),
            Arguments.of(CommentFieldMapper.Field.REPLIES, Comment_.REPLIES),
            Arguments.of(CommentFieldMapper.Field.PARENT_COMMENT, Comment_.PARENT_COMMENT),
            Arguments.of(CommentFieldMapper.Field.REACTIONS, Comment_.REACTIONS),
            Arguments.of(CommentFieldMapper.Field.DISLIKES, Comment_.DISLIKES),
            Arguments.of(CommentFieldMapper.Field.LIKES, Comment_.LIKES)
        );
    }

    private static Stream<Arguments> commentFieldNamesWithoutUnderscore() {
        return Stream.of(
            Arguments.of("commentId", Comment_.ID),
            Arguments.of("createdAt", Comment_.CREATED_AT),
            Arguments.of("isDeleted", Comment_.IS_DELETED),
            Arguments.of("parentComment", Comment_.PARENT_COMMENT)
        );
    }

    @ParameterizedTest
    @MethodSource("commentFieldNames")
    void shouldReturnCorrectCommentField(CommentFieldMapper.Field field, String expectedFieldName) {
        String commentFieldName = CommentFieldMapper.mapToCommentField(field.name().toLowerCase());
        assertThat(commentFieldName).isEqualTo(expectedFieldName);
    }

    @ParameterizedTest
    @MethodSource("commentFieldNamesWithoutUnderscore")
    void shouldReturnCorrectCommentFieldWhenFieldIsCamelCase(String field, String expectedFieldName) {
        String commentFieldName = CommentFieldMapper.mapToCommentField(field);
        assertThat(commentFieldName).isEqualTo(expectedFieldName);
    }

    @ParameterizedTest
    @MethodSource("commentFieldNames")
    void shouldNotReturnCommentFieldWhenFieldIsUppercase(CommentFieldMapper.Field field) {
        assertThatIllegalArgumentException().isThrownBy(() -> CommentFieldMapper.mapToCommentField(field.name()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDocumentFieldIsInvalid() {
        assertThatIllegalArgumentException().isThrownBy(() -> CommentFieldMapper.mapToCommentField("null"));
    }

}