package com.moviehub.mapper.dto;

import com.moviehub.dto.AddCommentRequest;
import com.moviehub.dto.CommentDetailsResponse;
import com.moviehub.dto.CommentPage;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CommentInfoMapperTest {

    private static final UUID FIRST_COMMENT_ID = UUID.fromString("bbefebac-b1d9-4a59-8a61-c04f2aeef660");
    private static final UUID SECOND_COMMENT_ID = UUID.fromString("d519841e-5826-4de2-8aca-3510f9e54cfc");

    private static final String FIRST_COMMENT_TEXT = "This is a comment";
    private static final String SECOND_COMMENT_TEXT = "Another comment";

    @Test
    void shouldMapToComment() {
        AddCommentRequest request = createAddCommentRequest();

        Comment result = CommentInfoMapper.mapToComment(request);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo(FIRST_COMMENT_TEXT);
    }

    @Test
    void shouldMapToCommentInfoDetailsResponse() {
        User user = createUser();
        Movie movie = createMovie();
        Comment comment = createComment(FIRST_COMMENT_ID, user, movie, FIRST_COMMENT_TEXT, null);
        Comment reply = createComment(SECOND_COMMENT_ID, user, movie, SECOND_COMMENT_TEXT, comment);
        comment.getReplies().add(reply);

        CommentDetailsResponse response = CommentInfoMapper.mapToCommentInfoDetailsResponse(comment);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(comment.getId());
        assertThat(response.getText()).isEqualTo(comment.getText());
        assertThat(response.getAuthor().getName()).isEqualTo(user.getName());
        assertThat(response.getReplies()).hasSize(1);
        assertThat(response.getReplies().getFirst().getId()).isEqualTo(SECOND_COMMENT_ID);
        assertThat(response.getReplies().getFirst().getText()).isEqualTo(SECOND_COMMENT_TEXT);
        assertThat(response.getReplies().getFirst().getAuthor().getName()).isEqualTo(user.getName());
    }

    @Test
    void shouldMapToCommentInfoPage() {
        List<Comment> comments = List.of(
            createComment(FIRST_COMMENT_ID, createUser(), createMovie(), FIRST_COMMENT_TEXT, null),
            createComment(SECOND_COMMENT_ID, createUser(), createMovie(), SECOND_COMMENT_TEXT, null)
        );

        Page<Comment> page = new PageImpl<>(comments, Pageable.ofSize(5), comments.size());

        CommentPage result = CommentInfoMapper.mapToCommentPage(page);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(comments.size());
        assertThat(result.getTotalElements()).isEqualTo(page.getTotalElements());
        assertThat(result.getTotalPages()).isEqualTo(page.getTotalPages());
    }

    private static AddCommentRequest createAddCommentRequest() {
        AddCommentRequest request = new AddCommentRequest();
        request.setText(FIRST_COMMENT_TEXT);
        return request;
    }

    private static User createUser() {
        return User.builder()
                   .id("auth0|1")
                   .name("user")
                   .pictureUrl("https://example.com/picture.jpg")
                   .build();
    }

    private static Movie createMovie() {
        return Movie.builder()
                    .name("Movie")
                    .build();
    }

    private static Comment createComment(UUID id, User user, Movie movie, String text, Comment parentComment) {
        return Comment.builder()
                      .id(id)
                      .text(text)
                      .createdAt(LocalDateTime.now())
                      .isDeleted(false)
                      .likes(5L)
                      .dislikes(2L)
                      .user(user)
                      .movie(movie)
                      .parentComment(parentComment)
                      .build();
    }

}